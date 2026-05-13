package ci.nsu.mobile.auth.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.data.model.dto.GroupDto
import ci.nsu.mobile.auth.data.model.dto.UserDto
import ci.nsu.mobile.auth.data.model.request.RegisterRequest
import ci.nsu.mobile.auth.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    var users     by mutableStateOf<List<UserDto>>(emptyList())
    var groups    by mutableStateOf<List<GroupDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error     by mutableStateOf<String?>(null)

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true; error = null
            repo.login(login, password)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message }
            isLoading = false
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true; error = null
            repo.register(request)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message }
            isLoading = false
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            repo.getUsers()
                .onSuccess { users = it }
                .onFailure { error = it.message }
            isLoading = false
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            repo.getGroups()
                .onSuccess { groups = it }
                .onFailure { error = it.message }
        }
    }

    fun logout() = TokenManager.clear()
}