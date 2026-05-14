package ci.nsu.mobile.main.ui.auth.login

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val loginError: String? = null,
    val passwordError: String? = null,
    val loginState: LoginState = LoginState.Idle,
    val hasNavigated: Boolean = false
)
