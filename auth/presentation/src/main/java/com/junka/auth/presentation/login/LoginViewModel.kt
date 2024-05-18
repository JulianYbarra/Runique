@file:OptIn(ExperimentalFoundationApi::class)

package com.junka.auth.presentation.login

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val event = Channel<LoginEvent>()
    val events = event.receiveAsFlow()

    init {
        combine(state.email.textAsFlow(), state.password.textAsFlow()) { email, password ->
            state = state.copy(
                canLogin = userDataValidator.isValidEmail(email.toString().trim()) &&
                        password.isNotEmpty()

            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()

            LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(isPasswordIsVisible = !state.isPasswordIsVisible)
            }

            else -> Unit
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(
                isLoggingIn = true
            )

            val result = authRepository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )

            when(result){
                is Result.Failure -> {
                    if (result.error == DataError.Network.UNAUTHORIZED){
                        event.send(LoginEvent.Error(
                            UiText.StringResource(R.string.error_email_password_incorrect)
                        ))
                    }else{
                        event.send(LoginEvent.Error(result.error.asUiText()))
                    }
                }
                is Result.Success -> {
                    event.send(LoginEvent.OnLoginSuccess)
                }
            }

            state = state.copy(
                isLoggingIn = false
            )
        }
    }
}