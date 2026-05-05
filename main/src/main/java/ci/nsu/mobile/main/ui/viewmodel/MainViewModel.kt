package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            AuthRepository.getUsers()
                .onSuccess { _users.value = it }
                .onFailure { _error.value = it.message }

            _loading.value = false
        }
    }

    fun logout() {
        TokenManager.clearToken()
        _users.value = emptyList()
    }

    fun clearError() {
        _error.value = null
    }
}