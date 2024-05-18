package com.junka.auth.presentation.register

import com.junka.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess : RegisterEvent
    data class Error(val error : UiText) : RegisterEvent
}