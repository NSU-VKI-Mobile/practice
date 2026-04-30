package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Список пользователей
    private val _usersState = MutableStateFlow<UiState<List<UserDto>>>(UiState.Loading)
    val usersState: StateFlow<UiState<List<UserDto>>> = _usersState.asStateFlow()

    // Событие выхода из системы
    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    init {
        // Загружаем пользователей сразу при создании ViewModel
        loadUsers()
    }

    public fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UiState.Loading
            try {
                val result = repository.getUsers()

                result.onSuccess { users ->
                    _usersState.value = UiState.Success(users)
                }.onFailure { exception ->
                    // Если ошибка 401 (сессия истекла), лучше выкинуть на логин
                    if (exception.message?.contains("401") == true || exception.message?.contains("Сессия") == true) {
                        _logoutEvent.emit(Unit)
                    } else {
                        _usersState.value = UiState.Error(exception.message ?: "Ошибка загрузки")
                    }
                }
            } catch (e: Exception) {
                _usersState.value = UiState.Error(e.message ?: "Ошибка")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Очищаем токен через репозиторий (или напрямую через TokenManager, если нужно)
            // Здесь достаточно сбросить состояние и отправить событие
            _logoutEvent.emit(Unit)
        }
    }
}