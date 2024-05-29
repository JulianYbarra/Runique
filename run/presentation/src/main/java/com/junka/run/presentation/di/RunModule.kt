package com.junka.run.presentation.di

import com.junka.run.domain.RunningTracker
import com.junka.run.presentation.active_run.ActiveRunViewModel
import com.junka.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runModule = module {

    singleOf(::RunningTracker)

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}