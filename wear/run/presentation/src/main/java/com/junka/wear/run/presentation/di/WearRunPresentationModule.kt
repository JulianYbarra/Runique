package com.junka.wear.run.presentation.di

import com.junka.wear.run.presentation.TrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val wearRunPresentationModule = module {
    viewModelOf(::TrackerViewModel)
}