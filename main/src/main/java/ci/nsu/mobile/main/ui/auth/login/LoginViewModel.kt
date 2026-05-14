package ci.nsu.mobile.main.ui.auth.login
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.datasource.local.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

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

    fun login(onSuccess: (String) -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            _uiState.update { it.copy(loginState = LoginState.Loading) }

            val result = repository.login(_uiState.value.login, _uiState.value.password)

            when (result) {
                is Result.Success -> {
                    if (!_uiState.value.hasNavigated) {
                        _uiState.update {
                            it.copy(
                                loginState = LoginState.Success(result.data),
                                hasNavigated = true
                            )
                        }
                        onSuccess(result.data)
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            loginState = LoginState.Error(result.message),
                            hasNavigated = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    fun resetNavigation() {
        _uiState.update { it.copy(hasNavigated = false) }
    }
}