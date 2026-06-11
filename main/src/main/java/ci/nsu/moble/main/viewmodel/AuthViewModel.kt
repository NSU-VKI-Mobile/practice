package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.dto.GroupDto
import ci.nsu.moble.main.data.dto.RegisterRequest
import ci.nsu.moble.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerState = _registerState.asStateFlow()

    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups = _groups.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            val result = repository.login(username, password)
            _loginState.value = if (result.isSuccess) {
                LoginUiState.Success
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "Ошибка входа")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = RegisterUiState.Loading
            val result = repository.register(request)
            _registerState.value = if (result.isSuccess) {
                RegisterUiState.Success
            } else {
                RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Ошибка регистрации")
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            if (result.isSuccess) {
                _groups.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    sealed class LoginUiState {
        object Idle : LoginUiState()
        object Loading : LoginUiState()
        object Success : LoginUiState()
        data class Error(val message: String) : LoginUiState()
    }

    sealed class RegisterUiState {
        object Idle : RegisterUiState()
        object Loading : RegisterUiState()
        object Success : RegisterUiState()
        data class Error(val message: String) : RegisterUiState()
    }
}