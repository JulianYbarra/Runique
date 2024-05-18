@file:OptIn(ExperimentalFoundationApi::class)

package com.junka.auth.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
import com.junka.auth.domain.PasswordValidationState

data class RegisterState(
    val email : TextFieldState = TextFieldState(),
    val isEmailValid : Boolean = false,
    val password : TextFieldState = TextFieldState(),
    val isPasswordIsVisible : Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering : Boolean = false,
    val canRegister : Boolean = false,
)