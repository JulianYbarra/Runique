package com.junka.core.data.di

import com.junka.core.data.auth.EncryptedSessionStorage
import com.junka.core.data.networking.HttpClientFactory
import com.junka.core.data.run.OfflineFirstRunRepository
import com.junka.core.domain.SessionStorage
import com.junka.core.domain.run.RunRepository
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build(CIO.create())
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}