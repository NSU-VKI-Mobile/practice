package ci.nsu.mobile.main.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateLogin(value: String) {
        _uiState.update {
            it.copy(
                login = value,
                loginError = null,
                loginState = LoginState.Idle
            )
        }
    }

    fun updatePassword(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                loginState = LoginState.Idle
            )
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        val currentState = _uiState.value

        if (currentState.login.isBlank()) {
            _uiState.update { it.copy(loginError = "Введите логин") }
            isValid = false
        }

        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Введите пароль") }
            isValid = false
        }

        return isValid
    }

    fun login(onSuccess: () -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            _uiState.update { it.copy(loginState = LoginState.Loading) }

            // TODO: Replace with actual API call
            delay(1000)

            // TODO: Handle actual response
            if (!_uiState.value.hasNavigated) {
                _uiState.update {
                    it.copy(
                        loginState = LoginState.Success("fake_token"),
                        hasNavigated = true
                    )
                }
                onSuccess()
            }
        }
    }

    fun resetNavigation() {
        _uiState.update { it.copy(hasNavigated = false) }
    }
}