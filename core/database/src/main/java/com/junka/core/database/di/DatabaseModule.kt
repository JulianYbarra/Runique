package com.junka.core.database.di

import androidx.room.Room
import com.junka.core.database.RoomLocalDataSource
import com.junka.core.database.RunDataBase
import com.junka.core.domain.run.LocalDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            RunDataBase::class.java,
            "run_database"
        ).build()
    }

    single { get<RunDataBase>().runDao }
    single { get<RunDataBase>().runPendingSyncDao }

    singleOf(::RoomLocalDataSource).bind<LocalDataSource>()
}