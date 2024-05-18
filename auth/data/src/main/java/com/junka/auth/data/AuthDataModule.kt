package com.junka.auth.data

import com.junka.auth.domain.AuthRepository
import com.junka.auth.domain.PatternValidator
import com.junka.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)

    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}