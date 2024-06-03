package com.junka.analytics.data.di

import com.junka.analytics.data.RoomAnalyticsRepository
import com.junka.analytics.domain.AnalyticsRepository
import com.junka.core.database.RunDataBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
    single {
        get<RunDataBase>().analyticsDao
    }
}