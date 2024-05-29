package com.junka.core.data.run

import com.junka.core.data.networking.get
import com.junka.core.database.dao.RunPendingSyncDao
import com.junka.core.database.mapper.toRun
import com.junka.core.domain.SessionStorage
import com.junka.core.domain.run.LocalDataSource
import com.junka.core.domain.run.RemoteDataSource
import com.junka.core.domain.run.Run
import com.junka.core.domain.run.RunRepository
import com.junka.core.domain.run.SyncRunScheduler
import com.junka.core.domain.util.DataError
import com.junka.core.domain.util.EmptyResult
import com.junka.core.domain.util.Result
import com.junka.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val applicationScope: CoroutineScope,
    private val pendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler,
    private val client: HttpClient
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteDataSource.getRuns()) {
            is Result.Failure -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertRuns(result.data)
                        .asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localDataSource.upsertRun(run)

        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val runWithId = run.copy(id = localResult.data)
        val remoteResult = remoteDataSource.postRun(runWithId, mapPicture)

        return when (remoteResult) {
            is Result.Failure -> {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        type = SyncRunScheduler.SyncType.CreateRun(
                            run = run,
                            mapPictureByte = mapPicture
                        )
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {

                applicationScope.async {
                    localDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(id: String) {
        localDataSource.deleteRun(id)

        // Edge case where the run is created in offline-mode,
        // and then deleted in offline-mode as well. In that case
        // we don't sync anything.
        val isPendingSync = pendingSyncDao.getRunPendingSyncEntity(runId = id) != null
        if (isPendingSync) {
            pendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteDataSource.deleteRun(id)
        }.await()

        if (remoteResult is Result.Failure) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(
                    type = SyncRunScheduler.SyncType.DeleteRun(id)
                )
            }.join()

        }
    }

    override suspend fun deleteAllRuns() {
        localDataSource.deleteAllRuns()
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val created = async {
                pendingSyncDao.getAllRunPendingSyncEntities(userId)
            }
            val deleted = async {
                pendingSyncDao.getAllDeletedRunSyncEntities(userId)
            }

            val createJobs = created
                .await()
                .map {
                    launch {
                        val run = it.run.toRun()
                        when (remoteDataSource.postRun(run, it.mapPictureByte)) {
                            is Result.Failure -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    pendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            val deleteJobs = deleted
                .await()
                .map {
                    launch {
                        when (remoteDataSource.deleteRun(it.runId)) {
                            is Result.Failure -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    pendingSyncDao.deleteDeletedRunSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        val result = client.get<Unit>(
            route = "/logout"
        ).asEmptyDataResult()

        client
            .plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()

        return result
    }
}