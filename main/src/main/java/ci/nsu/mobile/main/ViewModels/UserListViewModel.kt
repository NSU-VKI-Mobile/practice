package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.Data.Models.UserDto
import ci.nsu.mobile.main.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getUsers()
            if (result.isSuccess) {
                _users.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Ошибка загрузки"
            }
            _isLoading.value = false
        }
    }

    // suspend-функция с реальной логикой
    private suspend fun performLogout() {
        repository.logout()
        _logoutEvent.emit(Unit)
    }

    // Обычная функция для вызова из UI (запускает корутину)
    fun onLogoutClick() {
        viewModelScope.launch {
            performLogout()
        }
    }
}