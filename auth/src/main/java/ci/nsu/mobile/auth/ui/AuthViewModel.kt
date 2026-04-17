package ci.nsu.mobile.auth.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.*
import ci.nsu.mobile.auth.data.dto.GroupDto
import ci.nsu.mobile.auth.data.dto.RegisterRequest
import ci.nsu.mobile.auth.data.dto.UserDto
import ci.nsu.mobile.auth.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isUserLoggedIn by mutableStateOf(TokenManager.token != null && TokenManager.userId != -1)

    var usersList by mutableStateOf<List<UserDto>>(emptyList())
    var groupsList by mutableStateOf<List<GroupDto>>(emptyList())

    var sessionLogin by mutableStateOf("")
    var sessionPassword by mutableStateOf("")

    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups().onSuccess { groupsList = it }
                .onFailure { errorMessage = "Ошибка загрузки групп: ${it.message}" }
        }
    }

    fun login(login: String, pass: String) {
        if (login.isBlank() || pass.isBlank()) {
            errorMessage = "Заполните все поля"
            return
        }
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            repository.login(login, pass).onSuccess { response ->
                TokenManager.token = response.token
                sessionLogin = login
                sessionPassword = pass
                repository.getUsers().onSuccess { users ->
                    val myProfile = users.find { it.login == login }
                    TokenManager.userId = myProfile?.userId ?: -1
                    isUserLoggedIn = true
                }
            }.onFailure { errorMessage = "Ошибка входа: ${it.message}" }
            isLoading = false
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        isLoading = true
        viewModelScope.launch {
            repository.register(request).onSuccess { onSuccess() }
                .onFailure { errorMessage = "Ошибка регистрации: ${it.message}" }
            isLoading = false
        }
    }

    fun loadUsers() {
        isLoading = true
        viewModelScope.launch {
            repository.getUsers().onSuccess { usersList = it }
                .onFailure { errorMessage = "Не удалось загрузить пользователей" }
            isLoading = false
        }
    }

    fun logout() {
        TokenManager.clear()
        sessionLogin = ""
        sessionPassword = ""
        isUserLoggedIn = false
    }
}