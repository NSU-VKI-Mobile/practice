package com.example.auth.viewmodel.login

data class LoginState (
    val login: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val passwordState: Boolean = false,
    val isLoading: Boolean = false,
    val errorFields: Set<String> = emptySet()
)

sealed class LoginEvents {
    data class LoginChanged(val newLogin: String): LoginEvents()
    data class PasswordChanged(val newPassword: String): LoginEvents()
    data class PasswordVisibilityChanged(val newState: Boolean): LoginEvents()
    object SubmitLogin: LoginEvents()
    object CleanAll: LoginEvents()
}