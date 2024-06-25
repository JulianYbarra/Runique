package com.junka.run.data.di

import com.junka.core.domain.run.SyncRunScheduler
import com.junka.run.data.CreateRunWorker
import com.junka.run.data.DeleteRunWorker
import com.junka.run.data.FetchRunsWorker
import com.junka.run.data.SyncRunWorkerScheduler
import com.junka.run.data.connectivity.PhoneToWatchConnector
import com.junka.run.domain.WatchConnector
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}