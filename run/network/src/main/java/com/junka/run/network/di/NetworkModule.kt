package com.junka.run.network.di

import com.junka.core.domain.run.RemoteDataSource
import com.junka.run.network.KtorRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {

    singleOf(::KtorRemoteDataSource).bind<RemoteDataSource>()
}