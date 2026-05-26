package ci.nsu.moble.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val message: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Заполните все поля")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = repository.login(login, password)
            _loginState.value = when {
                result.isSuccess -> LoginState.Success("Вход выполнен успешно")
                else -> LoginState.Error(result.exceptionOrNull()?.message ?: "Ошибка входа")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}