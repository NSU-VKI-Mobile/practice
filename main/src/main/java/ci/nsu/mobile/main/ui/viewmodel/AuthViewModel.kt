package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.*
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

    private val _isLoggedIn = MutableStateFlow(repo.getToken() != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUsername = MutableStateFlow<String?>(null)
    val currentUsername: StateFlow<String?> = _currentUsername.asStateFlow()

    fun checkInitialAuthState() {
        _isLoggedIn.value = repo.getToken() != null
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            val result = repo.login(login, password)
            if (result.isSuccess) {
                _loginState.value = AuthState.Success
                _isLoggedIn.value = true
                _currentUsername.value = login
            } else {
                _loginState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка входа")
            }
        }
    }

    fun register(login: String, password: String, email: String, person: PersonDto) {
        viewModelScope.launch {
            val request = RegisterRequest(
                login = login,
                password = password,
                email = email,
                person = person
            )
            val result = repo.register(request)
            // Обработка результата при необходимости
        }
    }

    fun logout() {
        repo.logout()
        _isLoggedIn.value = false
        _loginState.value = AuthState.Idle
        _currentUsername.value = null
    }
}