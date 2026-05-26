package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.models.RegisterRequest
import ci.nsu.moble.main.data.models.UserDto
import ci.nsu.moble.main.data.repository.AuthApiResult
import ci.nsu.moble.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// состояние: loginState, registerState, users, isLoading, error
// методы: login(), register(), loadUsers(), logout()
// сейчас login() и register() — данные наобум (задержка 1 секунда)
// loadUsers() — данные наобум (возвращает 2 тестовых пользователя)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // состояние экрана входа
    private val _loginState = MutableStateFlow<AuthApiResult<Unit>?>(null)
    val loginState: StateFlow<AuthApiResult<Unit>?> = _loginState.asStateFlow()

    // состояние экрана регистрации
    private val _registerState = MutableStateFlow<AuthApiResult<Unit>?>(null)
    val registerState: StateFlow<AuthApiResult<Unit>?> = _registerState.asStateFlow()

    // крутилка загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // список пользователей
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    private val _usersLoading = MutableStateFlow(false)
    val usersLoading: StateFlow<Boolean> = _usersLoading.asStateFlow()

    // ошибки
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // вход (сейчас данные для примера, тк сервер комп не видит)
    fun login(login: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            val result = repository.login(login,password)
            _loginState.value = AuthApiResult.Success(Unit)
            _isLoading.value = false
        }
    }

    // регистрация (тоже аля-данные)
    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.register(request)
            when (result) {
                is AuthApiResult.Success -> {
                    _registerState.value = AuthApiResult.Success(Unit)
                }
                is AuthApiResult.Error -> {
                    _registerState.value = AuthApiResult.Error(result.message)
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }

    // сброс состояний
    fun clearStates() {
        _loginState.value = null
        _registerState.value = null
    }

    // загрузка пользователей (аля-данные, чтобы показывать интерфейс)
//    fun loadUsers() {
//        viewModelScope.launch {
//            _usersLoading.value = true
//            _error.value = null
//            kotlinx.coroutines.delay(1000)
//            _users.value = listOf(
//                UserDto(1, "alex", "alex@mail.com", "+79123456789", null),
//                UserDto(2, "maria", "maria@mail.com", "+79234567890", null)
//            )
//            _usersLoading.value = false
//        }
//    }
    fun loadUsers() {
        viewModelScope.launch {
            _usersLoading.value = true
            _error.value = null
            val result = repository.getUsers()
            when (result) {
                is AuthApiResult.Success -> {
                    _users.value = result.data
                }
                is AuthApiResult.Error -> {
                    _error.value = result.message
                }
                else -> {}
            }
            _usersLoading.value = false
        }
    }

    // выход
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}