package com.junka.core.domain

data class AuthInfo(
    val accessToken : String,
    val refreshToken : String,
    val userId : String
)