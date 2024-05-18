package com.junka.core.data.di

import com.junka.core.data.auth.EncryptedSessionStorage
import com.junka.core.data.networking.HttpClientFactory
import com.junka.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}