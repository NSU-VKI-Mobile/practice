package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.Result
import ci.nsu.mobile.main.data.repository.Result.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val loginState: StateFlow<Result<Unit>> = _loginState

    private val _registerState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val registerState: StateFlow<Result<Unit>> = _registerState

    private val _usersState = MutableStateFlow<Result<List<UserDto>>>(Result.Loading)
    val usersState: StateFlow<Result<List<UserDto>>> = _usersState

    private val _groupsState = MutableStateFlow<Result<List<GroupDto>>>(Result.Loading)
    val groupsState: StateFlow<Result<List<GroupDto>>> = _groupsState

    // Флаг авторизации
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        checkAuth()
    }
    private fun checkAuth() {
        _isAuthenticated.value = TokenManager.token != null
        if (_isAuthenticated.value) {
            loadUsers()
        } else {
            _loginState.value = Result.Idle // Было Success(Unit), стало Idle
            _registerState.value = Result.Idle
        }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Result.Loading
            when (val result = AuthRepository.login(login, password)) {
                is Result.Success -> {
                    _isAuthenticated.value = true
                    _loginState.value = Success(Unit)
                    loadUsers()
                }
                is Result.Error -> _loginState.value = Error(result.message)
                Result.Loading -> {}
                Result.Idle -> TODO()
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = Result.Loading
            when (val result = AuthRepository.register(request)) {
                is Success -> {
                    _registerState.value = Success(Unit)
                    // Опционально: можно сразу сделать логин, если сервер возвращает токен при регистрации
                    // Или просто оставить как есть, чтобы пользователь сам перешел на экран входа
                }
                is Error -> _registerState.value = Error(result.message)
                Loading -> {}
                Idle -> TODO()
            }
        }
    }

    fun loadUsers() {
        android.util.Log.d("AuthViewModel", "loadUsers called") // 👈 ДОБАВЬТЕ ЭТУ СТРОКУ
        viewModelScope.launch {
            _usersState.value = Result.Loading
            try {
                val users = AuthRepository.getUsers()
                _usersState.value = users
                android.util.Log.d("AuthViewModel", "Users loaded: ${users}")
            } catch (e: Exception) {
                _usersState.value = Result.Error(e.message ?: "Ошибка")
                android.util.Log.e("AuthViewModel", "Error loading users", e)
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _groupsState.value = Result.Loading
            _groupsState.value = AuthRepository.getGroups()
        }
    }

    fun logout() {
        AuthRepository.logout()
        _isAuthenticated.value = false
        _usersState.value = Result.Success(emptyList())
        _loginState.value = Result.Idle
        _registerState.value = Result.Idle
    }

    // Метод для сброса ошибки регистрации, чтобы пользователь мог попробовать снова
    fun resetRegisterError() {
        if (_registerState.value is Result.Error) {
            _registerState.value = Result.Success(Unit)
        }
    }
}