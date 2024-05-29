package com.junka.core.database

import android.database.sqlite.SQLiteFullException
import com.junka.core.database.dao.RunDao
import com.junka.core.database.mapper.toRun
import com.junka.core.database.mapper.toRunEntity
import com.junka.core.domain.run.LocalDataSource
import com.junka.core.domain.run.Run
import com.junka.core.domain.run.RunId
import com.junka.core.domain.util.DataError
import com.junka.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalDataSource(
    private val runDao: RunDao
) : LocalDataSource {
    override fun getRuns(): Flow<List<Run>> =
        runDao.getRuns()
            .map { runEntities ->
                runEntities.map { it.toRun() }
            }

    override suspend fun upsertRun(run: Run): Result<RunId, DataError.Local> {
        return try {
            val entity = run.toRunEntity()
            runDao.upsertRun(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Failure(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local> {
        return try {
            val entities = runs.map { it.toRunEntity()  }
            runDao.upsertRuns(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Failure(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteRun(id: String) {
        runDao.deleteRun(id)
    }

    override suspend fun deleteAllRuns() {
        runDao.deleteAllRuns()
    }
}