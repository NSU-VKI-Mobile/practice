package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.repository.AuthRepository
import ci.nsu.mobile.main.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    fun onLoginChange(login: String) {
        _loginState.value = _loginState.value.copy(login = login, loginError = null)
    }

    fun onPasswordChange(password: String) {
        _loginState.value = _loginState.value.copy(password = password, passwordError = null)
    }

    fun login() {
        val login = _loginState.value.login
        val password = _loginState.value.password

        var hasError = false

        if (login.isEmpty()) {
            _loginState.value = _loginState.value.copy(loginError = "Введите логин")
            hasError = true
        }

        if (password.isEmpty()) {
            _loginState.value = _loginState.value.copy(passwordError = "Введите пароль")
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null)

            val result = repository.login(login, password)

            when (result) {
                is Result.Success -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                is Result.Error -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {}
            }
        }
    }

    fun resetSuccess() {
        _loginState.value = _loginState.value.copy(isSuccess = false)
    }

    data class LoginUiState(
        val login: String = "",
        val password: String = "",
        val loginError: String? = null,
        val passwordError: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSuccess: Boolean = false
    )
}