package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Состояние всего нашего интерфейса в одном месте
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val groups: List<GroupDto> = emptyList()
)

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // При запуске ViewModel проверяем, есть ли у нас уже токен.
        // Если да — сразу пускаем на главный экран.
        if (TokenManager.token != null) {
            _uiState.update { it.copy(isAuthenticated = true) }
        }
    }

    fun login(login: String, pass: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = repository.login(login, pass)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Ошибка входа") }
            }
        }
    }

    fun fetchGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            result.onSuccess { groups ->
                _uiState.update { it.copy(groups = groups) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message ?: "Не удалось загрузить группы") }
            }
        }
    }

    // В onSuccess передаем коллбэк, чтобы после успешной регистрации интерфейс сам перекинул нас на экран входа
    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = repository.register(request)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Ошибка регистрации") }
            }
        }
    }

    fun fetchUsers() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = repository.getUsers()
            result.onSuccess { users ->
                _uiState.update { it.copy(isLoading = false, users = users) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Не удалось загрузить пользователей") }
            }
        }
    }

    fun logout() {
        TokenManager.clear() // Удаляем токен из памяти телефона
        _uiState.value = AuthUiState() // Полностью сбрасываем состояние UI
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}