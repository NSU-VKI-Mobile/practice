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

    fun setValidationError(message: String) {
        error = message
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        if (login.isBlank() || password.isBlank()) {
            error = "Заполните логин и пароль"
            return
        }
        viewModelScope.launch {
            isLoading = true
            error = null
            try{
                repository.login(login, password).onSuccess {
                    isLoggedIn = true
                    onSuccess()
                }.onFailure {
                    error = it.message ?: "Ошибка входа"
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                repository.register(request).onSuccess {
                    onSuccess()
                }.onFailure {
                    error = it.message ?: "Ошибка регистрации"
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                repository.getUsers().onSuccess { users = it }
                    .onFailure { error = it.message ?: "Ошибка загрузки пользователей" }
            } finally {
                isLoading = false
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                repository.getGroups().onSuccess { groups = it }
                    .onFailure { error = it.message ?: "Ошибка загрузки групп" }
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        repository.logout()
        isLoggedIn = false
        users = emptyList()
        groups = emptyList()
        error = null
    }

    fun clearError() {
        error = null
    }
}