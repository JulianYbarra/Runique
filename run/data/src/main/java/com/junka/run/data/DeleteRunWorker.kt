package com.junka.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.junka.core.database.dao.RunPendingSyncDao
import com.junka.core.domain.run.RemoteDataSource

class DeleteRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteRunDataSource: RemoteDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }


        val runId = params.inputData.getString(RUN_ID) ?: return Result.failure()


        return when (val result = remoteRunDataSource.deleteRun(runId)) {
            is com.junka.core.domain.util.Result.Failure -> result.error.toWorkerResult()
            is com.junka.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteDeletedRunSyncEntity(runId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}