package com.junka.core.android_testing

import com.junka.core.domain.AuthInfo
import com.junka.core.domain.SessionStorage

class SessionStorageFake : SessionStorage {

    private var authInfo: AuthInfo? = null

    override suspend fun get(): AuthInfo? {
        return authInfo
    }

    override suspend fun set(info: AuthInfo?) {
        authInfo = info
    }
}