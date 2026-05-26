package ci.nsu.moble.main.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.auth.data.models.RegisterRequest
import ci.nsu.moble.main.auth.data.models.UserDto
import ci.nsu.moble.main.auth.data.repository.AuthApiResult
import ci.nsu.moble.main.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // Состояние входа
    private val _loginState = MutableStateFlow<AuthApiResult<Unit>?>(null)
    val loginState: StateFlow<AuthApiResult<Unit>?> = _loginState.asStateFlow()

    // Состояние регистрации
    private val _registerState = MutableStateFlow<AuthApiResult<Unit>?>(null)
    val registerState: StateFlow<AuthApiResult<Unit>?> = _registerState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Состояние списка пользователей
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    private val _usersLoading = MutableStateFlow(false)
    val usersLoading: StateFlow<Boolean> = _usersLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _loginState.value = AuthApiResult.Success(Unit)
            _isLoading.value = false
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _registerState.value = AuthApiResult.Success(Unit)
            _isLoading.value = false
        }
    }

    fun clearStates() {
        _loginState.value = null
        _registerState.value = null
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersLoading.value = true
            _error.value = null
            kotlinx.coroutines.delay(1000)
            _users.value = listOf(
                UserDto(1, "alex", "alex@mail.com", "+79123456789", null),
                UserDto(2, "maria", "maria@mail.com", "+79234567890", null)
            )
            _usersLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}