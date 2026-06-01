package com.example.auth.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.data.repository.AuthRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun loginEvent(event: LoginEvents) {
        when(event) {
            is LoginEvents.LoginChanged -> {
                _state.update { it.copy(login = event.newLogin,
                    errorFields = it.errorFields - "Login") }
            }
            is LoginEvents.PasswordChanged -> {
                _state.update { it.copy(password = event.newPassword,
                    errorFields = it.errorFields - "Password" ) }
            }
            is LoginEvents.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordState = event.newState) }
            }
            is LoginEvents.SubmitLogin -> if (validationLoginScreen()) login()
            is LoginEvents.CleanAll ->  resetState()
        }
    }

    private fun validationLoginScreen(): Boolean {
        val errorFields = mutableSetOf<String>()

        if (_state.value.login.isEmpty()) {
            errorFields.add("Login")
        }
        if (_state.value.password.isEmpty()) {
            errorFields.add("Password")
        }

        _state.update {
            it.copy(
                errorFields = errorFields,
                errorMessage = when {
                    errorFields.size == 2 -> "Введите логин и пароль"
                    errorFields.contains("Login") -> "Введите логин"
                    errorFields.contains("Password") -> "Введите пароль"
                    else -> null
                }
            )
        }

        return errorFields.isEmpty()
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = repository.login(_state.value.login, _state.value.password)
            if (result.isSuccess) {
                _state.update { it.copy(isSuccess = true, isLoading = false, errorMessage = null) }
            }
            if (result.isFailure) {
                _state.update { it.copy(isSuccess = false, isLoading = false ,errorMessage = "Ошибка входа. Проверьте введенные данные")}
            }
        }
    }

    private fun resetState() {
        _state.update {
            it.copy(
                login = "",
                password = "",
                errorMessage = null,
                isSuccess = false,
                errorFields = emptySet()
            )
        }
    }
}