@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@file:OptIn(ExperimentalFoundationApi::class)

package com.junka.auth.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junka.auth.domain.AuthRepository
import com.junka.auth.domain.UserDataValidator
import com.junka.auth.presentation.R
import com.junka.core.domain.util.DataError
import com.junka.core.domain.util.Result
import com.junka.core.presentation.ui.UiText
import com.junka.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val event = Channel<RegisterEvent>()
    val events = event.receiveAsFlow()

    init {
        state.email.textAsFlow()
            .onEach { email ->
                val isValidEmail = userDataValidator.isValidEmail(email.toString())
                state = state.copy(
                    isEmailValid = isValidEmail,
                    canRegister = isValidEmail && state.passwordValidationState.isValid && !state.isRegistering
                )
            }
            .launchIn(viewModelScope)

        state.password.textAsFlow()
            .onEach { password ->
                val passwordValidationState =
                    userDataValidator.validatePassword(password.toString())
                state = state.copy(
                    passwordValidationState = passwordValidationState,
                    canRegister = state.isEmailValid && passwordValidationState.isValid && !state.isRegistering
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnRegisterClick -> {
                register()
            }
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(isPasswordIsVisible = !state.isPasswordIsVisible)
            }
            else -> Unit
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)
            val result = authRepository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isRegistering = false)

            when (result) {
                is Result.Failure -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        event.send(
                            RegisterEvent.Error(
                                UiText.StringResource(R.string.email_already_exists)
                            )
                        )
                    } else {
                        event.send(RegisterEvent.Error(result.error.asUiText()))
                    }
                }

                is Result.Success -> {
                    event.send(RegisterEvent.RegistrationSuccess)
                }
            }
        }
    }
}