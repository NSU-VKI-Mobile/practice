package ci.nsu.mobile.main.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.model.GroupDto
import ci.nsu.mobile.main.model.RegisterRequest
import ci.nsu.mobile.main.model.UserDto
import ci.nsu.mobile.main.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var users by mutableStateOf<List<UserDto>>(emptyList())
        private set

    var groups by mutableStateOf<List<GroupDto>>(emptyList())
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null

            var result = repository.login(login, password)

            isLoading = false

            result.onSuccess {
                isLoggedIn = true
                onSuccess()
            }

            result.onFailure {
                error = it.message
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true

            val result = repository.getUsers()

            isLoading = false

            result.onSuccess { users = it }
            result.onFailure { error = it.message }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            result.onSuccess { groups = it }
        }
    }

    fun logout() {
        repository.logout()
        isLoggedIn = false
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null

            val result = repository.register(request)

            isLoading = false
            result.onSuccess {
                onSuccess()
            }

            result.onFailure {
                error = it.message
            }
        }
    }

    fun clearError() {
        error = null
    }
}