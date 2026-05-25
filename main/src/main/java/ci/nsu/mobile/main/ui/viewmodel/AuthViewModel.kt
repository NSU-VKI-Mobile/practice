package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    // 🟢 НОВОЕ: Реактивное состояние входа
    private val _isLoggedIn = MutableStateFlow(repo.getToken() != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            val result = repo.login(username, password)
            if (result.isSuccess) {
                _loginState.value = AuthState.Success
                _isLoggedIn.value = true // Обновляем статус
            } else {
                _loginState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка входа")
            }
        }
    }

    fun register(username: String, password: String, email: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            val result = repo.register(username, password, email)
            if (result.isSuccess) {
                _registerState.value = AuthState.Success
            } else {
                _registerState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка регистрации")
            }
        }
    }

    fun logout() {
        repo.logout()
        _isLoggedIn.value = false // 🟢 Ключевой момент: меняем статус на false
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
    }

    fun resetStates() {
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
    }

    // Этот метод теперь можно использовать для первоначальной проверки,
    // но основной поток идет через StateFlow isLoggedIn
    fun checkInitialAuthState() {
        _isLoggedIn.value = repo.getToken() != null
    }
}