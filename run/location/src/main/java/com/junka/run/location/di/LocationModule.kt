package com.junka.run.location.di

import com.junka.run.domain.LocationObserver
import com.junka.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module{
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()
}