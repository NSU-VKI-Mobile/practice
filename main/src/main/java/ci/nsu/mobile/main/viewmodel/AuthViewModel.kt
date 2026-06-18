package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NavEvent {
    object GoToMain : NavEvent()
    object GoToLogin : NavEvent()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Состояние для входа/регистрации
    private val _uiState = MutableStateFlow<UiState<UserDto>>(UiState.Idle)
    val uiState: StateFlow<UiState<UserDto>> = _uiState.asStateFlow()

    // Состояние для списка групп
    private val _groupsState = MutableStateFlow<UiState<List<GroupDto>>>(UiState.Loading)
    val groupsState: StateFlow<UiState<List<GroupDto>>> = _groupsState.asStateFlow()

    // События навигации
    private val _navEvent = MutableSharedFlow<NavEvent>()
    val navEvent: SharedFlow<NavEvent> = _navEvent.asSharedFlow()

    init {
        loadGroups()
    }

    // Загрузка групп
    private fun loadGroups() {
        viewModelScope.launch {
            _groupsState.value = UiState.Loading
            try {
                val result = repository.getGroups()
                result.onSuccess { groups ->
                    _groupsState.value = UiState.Success(groups)
                }.onFailure { exception ->
                    _groupsState.value = UiState.Error(exception.message ?: "Ошибка загрузки групп")
                }
            } catch (e: Exception) {
                _groupsState.value = UiState.Error(e.message ?: "Ошибка")
            }
        }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.login(login, password)
                result.onSuccess { user ->
                    _uiState.value = UiState.Success(user)
                    _navEvent.emit(NavEvent.GoToMain)
                }.onFailure { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Ошибка входа")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.register(request)
                result.onSuccess {
                    _uiState.value = UiState.Idle
                    _navEvent.emit(NavEvent.GoToLogin)
                }.onFailure { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Ошибка регистрации")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun clearError() {
        _uiState.value = UiState.Idle
    }
}