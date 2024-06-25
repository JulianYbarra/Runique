package com.junka.wear.run.data.di

import com.junka.wear.run.data.HealthServicesExerciseTracker
import com.junka.wear.run.data.connectivity.WatchToPhoneConnector
import com.junka.wear.run.domain.ExerciseTracker
import com.junka.wear.run.domain.PhoneConnector
import com.junka.wear.run.domain.RunningTracker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()
    singleOf(::WatchToPhoneConnector).bind<PhoneConnector>()
    singleOf(::RunningTracker)
    single {
        get<RunningTracker>().elapsedTime
    }
}