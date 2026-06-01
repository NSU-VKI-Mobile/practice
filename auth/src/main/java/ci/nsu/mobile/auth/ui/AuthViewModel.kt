package ci.nsu.mobile.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.auth.AuthState
import ci.nsu.mobile.domain.model.GroupDto
import ci.nsu.mobile.domain.model.PersonDto
import ci.nsu.mobile.domain.model.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val manager: AuthManager) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    // Состояние входа берем из менеджера
    private val _isLoggedIn = MutableStateFlow(manager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUsername = MutableStateFlow<String?>(manager.getCurrentUserLogin())
    val currentUsername: StateFlow<String?> = _currentUsername.asStateFlow()

    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups.asStateFlow()

    private val _users = MutableStateFlow<List<ci.nsu.mobile.domain.model.UserDto>>(emptyList())
    val users: StateFlow<List<ci.nsu.mobile.domain.model.UserDto>> = _users.asStateFlow()

    fun checkInitialAuthState() {
        _isLoggedIn.value = manager.isLoggedIn()
        _currentUsername.value = manager.getCurrentUserLogin()
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            val result = manager.login(login, password)
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
            _registerState.value = AuthState.Loading
            val request = RegisterRequest(
                login = login,
                password = password,
                email = email,
                person = person
            )
            val result = manager.register(request)
            if (result.isSuccess) {
                _registerState.value = AuthState.Success
            } else {
                _registerState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка регистрации")
            }
        }
    }

    fun logout() {
        manager.logout()
        _isLoggedIn.value = false
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
        _currentUsername.value = null
    }

    fun resetStates() {
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
    }

    fun loadGroups() {
        viewModelScope.launch {
            val result = manager.getGroups()
            if (result.isSuccess) {
                _groups.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            val result = manager.getUsers()
            if (result.isSuccess) {
                _users.value = result.getOrNull() ?: emptyList()
            }
        }
    }
}