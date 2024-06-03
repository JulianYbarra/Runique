package com.junka.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.junka.core.database.dao.AnalyticsDao
import com.junka.core.database.dao.RunDao
import com.junka.core.database.dao.RunPendingSyncDao
import com.junka.core.database.entity.DeletedRunSyncEntity
import com.junka.core.database.entity.RunEntity
import com.junka.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RunDataBase : RoomDatabase() {

    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
    abstract val analyticsDao : AnalyticsDao
}