package com.junka.run.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.junka.core.database.dao.RunPendingSyncDao
import com.junka.core.database.entity.DeletedRunSyncEntity
import com.junka.core.database.entity.RunPendingSyncEntity
import com.junka.core.database.mapper.toRunEntity
import com.junka.core.domain.SessionStorage
import com.junka.core.domain.run.Run
import com.junka.core.domain.run.RunId
import com.junka.core.domain.run.SyncRunScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
) : SyncRunScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncRunScheduler.SyncType) {
        when (type) {
            is SyncRunScheduler.SyncType.CreateRun -> scheduleCreateRunWorker(
                run = type.run,
                mapPictureByte = type.mapPictureByte
            )

            is SyncRunScheduler.SyncType.DeleteRun -> scheduleDeleteRunWorker(type.runId)
            is SyncRunScheduler.SyncType.FetchRuns -> schedulerFetchRunWorker(type.interval)
        }
    }

    private suspend fun scheduleDeleteRunWorker(runId: RunId) {
        val userId = sessionStorage.get()?.userId ?: return
        val deleteRun = DeletedRunSyncEntity(
            runId = runId,
            userId = userId
        )

        pendingSyncDao.upsertDeletedRunSyncEntity(deleteRun)

        val workerRequest = OneTimeWorkRequestBuilder<DeleteRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .addTag("delete_work")
            .setInputData(
                Data.Builder()
                    .putString(DeleteRunWorker.RUN_ID, runId)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workerRequest).await()
        }.join()
    }

    private suspend fun scheduleCreateRunWorker(run: Run, mapPictureByte: ByteArray) {
        val userId = sessionStorage.get()?.userId ?: return

        val pendingRun = RunPendingSyncEntity(
            userId = userId,
            run = run.toRunEntity(),
            mapPictureByte = mapPictureByte
        )

        pendingSyncDao.upsertRunPendingSyncEntity(pendingRun)

        val workerRequest = OneTimeWorkRequestBuilder<CreateRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .addTag("create_work")
            .setInputData(
                Data.Builder()
                    .putString(CreateRunWorker.RUN_ID, run.id)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workerRequest).await()
        }.join()
    }

    private suspend fun schedulerFetchRunWorker(interval: Duration) {
        val isSyncScheduler = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduler) {
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchRunsWorker>(
            repeatInterval = interval.toJavaDuration()
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("sync_work")
            .build()

        workManager.enqueue(workRequest).await()
    }

    override suspend fun cancelAllSync() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

}