package com.junka.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.junka.core.domain.run.RunRepository

class FetchRunsWorker(
    context: Context, params: WorkerParameters, private val repository: RunRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        return when (val result = repository.fetchRuns()) {
            is com.junka.core.domain.util.Result.Failure -> {
                result.error.toWorkerResult()
            }

            is com.junka.core.domain.util.Result.Success -> Result.success()
        }
    }

}