package com.junka.auth.domain

import com.junka.core.domain.util.DataError
import com.junka.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun login(email : String,password : String) : EmptyResult<DataError.Network>
    suspend fun register(email: String, password: String) : EmptyResult<DataError.Network>
}