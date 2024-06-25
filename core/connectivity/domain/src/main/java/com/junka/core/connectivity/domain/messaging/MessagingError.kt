package com.junka.core.connectivity.domain.messaging

import com.junka.core.domain.util.Error

enum class MessagingError : Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}