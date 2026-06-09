package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepositoryImpl
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

class AuthViewModel(private val repo: AuthRepositoryImpl) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    // 🟢 Используем TokenManager.isLoggedIn()
    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUsername = MutableStateFlow<String?>(null)
    val currentUsername: StateFlow<String?> = _currentUsername.asStateFlow()


    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups.asStateFlow()

    fun checkInitialAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
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
            _registerState.value = AuthState.Loading
            val request = RegisterRequest(
                login = login,
                password = password,
                email = email,
                person = person
            )
            val result = repo.register(request)
            if (result.isSuccess) {
                _registerState.value = AuthState.Success
            } else {
                _registerState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Ошибка регистрации")
            }
        }
    }

    fun logout() {
        repo.logout()
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
            val result = repo.getGroups()
            if (result.isSuccess) {
                _groups.value = result.getOrNull() ?: emptyList()
            }
        }
    }
}