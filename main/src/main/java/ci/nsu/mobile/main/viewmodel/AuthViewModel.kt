package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.RegisterRequest  // <-- ДОБАВЬТЕ ЭТУ СТРОКУ
import ci.nsu.mobile.main.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            val result = repository.login(login, password)
            _loginState.value = if (result.isSuccess) {
                AuthState.Success("Login successful")
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            val result = repository.register(request)
            _registerState.value = if (result.isSuccess) {
                AuthState.Success("Registration successful")
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun resetStates() {
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
    }
}