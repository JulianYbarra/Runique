package com.junka.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.junka.core.database.dao.RunPendingSyncDao
import com.junka.core.database.mapper.toRun
import com.junka.core.domain.run.RemoteDataSource
import com.junka.core.domain.util.Result

class CreateRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteDataSource: RemoteDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingRunId = params.inputData.getString(RUN_ID) ?: return Result.failure()

        val pendingRunEntity =
            pendingSyncDao.getRunPendingSyncEntity(pendingRunId) ?: return Result.failure()

        val run = pendingRunEntity.run.toRun()

        return when (val result = remoteDataSource.postRun(run, pendingRunEntity.mapPictureByte)) {
            is com.junka.core.domain.util.Result.Failure -> {
                result.error.toWorkerResult()
            }

            is com.junka.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteRunPendingSyncEntity(pendingRunId)
                Result.success()
            }
        }

    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}