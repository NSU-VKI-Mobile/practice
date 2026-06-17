package ci.nsu.mobile.main.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.GroupDto
import ci.nsu.mobile.main.data.RegisterRequest
import ci.nsu.mobile.main.data.TokenManager
import ci.nsu.mobile.main.data.UserDto
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isUserLoggedIn by mutableStateOf(TokenManager.token != null)

    var usersList by mutableStateOf<List<UserDto>>(emptyList())
    var groupsList by mutableStateOf<List<GroupDto>>(emptyList())

    /*
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
    */

    fun loadGroups() {
        groupsList = listOf(
            GroupDto(id = 1, name = "Группа ИВТ-21"),
            GroupDto(id = 2, name = "Группа ИВТ-22"),
            GroupDto(id = 3, name = "Группа ПИ-21"),
            GroupDto(id = 4, name = "Группа ПИ-22")
        )
    }

    fun login(login: String, pass: String) {
        if (login.isBlank() || pass.isBlank()) {
            errorMessage = "Заполните все поля"
            return
        }
        isLoading = true
        errorMessage = null

        TokenManager.token = "fake_jwt_token_12345"
        isUserLoggedIn = true
        isLoading = false
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null

        onSuccess()
        isLoading = false
    }

    fun loadUsers() {
        isLoading = true

        usersList = listOf(
            UserDto(
                userId = 1,
                login = "ivanov",
                email = "ivanov@mail.ru",
                phoneNumber = "+7-999-123-45-67",
                roleId = 1,
                authAllowed = true,
                personId = 101,
                createdDate = "2024-01-01",
                lastLoginDate = "2024-01-15"
            ),
            UserDto(
                userId = 2,
                login = "petrov",
                email = "petrov@mail.ru",
                phoneNumber = "+7-999-765-43-21",
                roleId = 1,
                authAllowed = true,
                personId = 102,
                createdDate = "2024-01-02",
                lastLoginDate = "2024-01-16"
            ),
            UserDto(
                userId = 3,
                login = "sidorov",
                email = "sidorov@mail.ru",
                phoneNumber = null,
                roleId = 1,
                authAllowed = true,
                personId = 103,
                createdDate = "2024-01-03",
                lastLoginDate = null
            )
        )
        isLoading = false
    }

    fun logout() {
        TokenManager.clear()
        isUserLoggedIn = false
    }
}