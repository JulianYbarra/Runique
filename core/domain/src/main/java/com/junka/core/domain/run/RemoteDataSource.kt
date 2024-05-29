package com.junka.core.domain.run

import com.junka.core.domain.util.DataError
import com.junka.core.domain.util.EmptyResult
import com.junka.core.domain.util.Result

interface RemoteDataSource {
    suspend fun getRuns(): Result<List<Run>, DataError.Network>
    suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network>
    suspend fun deleteRun(id: String): EmptyResult<DataError.Network>
}