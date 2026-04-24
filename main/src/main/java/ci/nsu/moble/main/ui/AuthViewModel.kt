package ci.nsu.mobile.main.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.*
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isUserLoggedIn by mutableStateOf(TokenManager.token != null)

    var usersList by mutableStateOf<List<UserDto>>(emptyList())
    var groupsList by mutableStateOf<List<GroupDto>>(emptyList())

    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess {
                    groupsList = it
                }
                .onFailure { error ->
                    error.printStackTrace()
                    errorMessage = "Ошибка загрузки групп: ${error.message}"
                }
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
            repository.login(login, pass)
                .onSuccess { response ->
                    TokenManager.token = response.token
                    isUserLoggedIn = true
                }
                .onFailure { error ->
                    errorMessage = "Ошибка входа: ${error.message}"
                }
            isLoading = false
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            repository.register(request)
                .onSuccess { onSuccess() }
                .onFailure { error -> errorMessage = "Ошибка регистрации: ${error.message}" }
            isLoading = false
        }
    }

    fun loadUsers() {
        isLoading = true
        viewModelScope.launch {
            repository.getUsers()
                .onSuccess { usersList = it }
                .onFailure { errorMessage = "Не удалось загрузить пользователей" }
            isLoading = false
        }
    }

    fun logout() {
        TokenManager.clear()
        isUserLoggedIn = false
    }
}