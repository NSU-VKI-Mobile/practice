package ci.nsu.mobile.main

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.api.ApiClient
import ci.nsu.mobile.main.data.model.dto.*
import ci.nsu.mobile.main.data.model.request.*
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository(ApiClient.api)
    var users by mutableStateOf<List<UserDto>>(emptyList())
    var groups by mutableStateOf<List<GroupDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val result = repo.login(login, password)
            isLoading = false

            result.onSuccess { onSuccess() }.onFailure { error = it.message }
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val result = repo.register(request)
            isLoading = false

            result.onSuccess { onSuccess() }.onFailure { error = it.message }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            val result = repo.getUsers()
            isLoading = false

            result.onSuccess { users = it }.onFailure { error = it.message }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            val result = repo.getGroups()
            result.onSuccess { groups = it }
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}