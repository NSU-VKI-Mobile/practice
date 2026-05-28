package com.example.authapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.data.TokenManager
import com.example.authapp.data.model.*
import com.example.authapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Состояние всего приложения
data class AuthUiState(
    // Экран логина
    val loginText: String = "",
    val passwordText: String = "",

    // Экран регистрации
    val regLogin: String = "",
    val regPassword: String = "",
    val regEmail: String = "",
    val regPhone: String = "",
    val regFirstName: String = "",
    val regLastName: String = "",
    val regMiddleName: String = "",
    val regBirthDate: String = "",
    val regGender: String = "M",
    val regGroupId: Int = 0,

    // Данные с сервера
    val groups: List<GroupDto> = emptyList(),
    val users: List<UserDto> = emptyList(),

    // Флаги состояния
    val isLoading: Boolean = false,      // показывать прогресс-бар?
    val isLoggedIn: Boolean = false,     // залогинен ли пользователь?
    val isRegistered: Boolean = false,   // успешна ли регистрация?
    val errorMessage: String? = null     // текст ошибки
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // --- Поля логина ---
    fun onLoginChanged(value: String) {
        _uiState.update { it.copy(loginText = value, errorMessage = null) }
    }
    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(passwordText = value, errorMessage = null) }
    }

    // --- Поля регистрации ---
    fun onRegLoginChanged(v: String) { _uiState.update { it.copy(regLogin = v) } }
    fun onRegPasswordChanged(v: String) { _uiState.update { it.copy(regPassword = v) } }
    fun onRegEmailChanged(v: String) { _uiState.update { it.copy(regEmail = v) } }
    fun onRegPhoneChanged(v: String) { _uiState.update { it.copy(regPhone = v) } }
    fun onRegFirstNameChanged(v: String) { _uiState.update { it.copy(regFirstName = v) } }
    fun onRegLastNameChanged(v: String) { _uiState.update { it.copy(regLastName = v) } }
    fun onRegMiddleNameChanged(v: String) { _uiState.update { it.copy(regMiddleName = v) } }
    fun onRegBirthDateChanged(v: String) { _uiState.update { it.copy(regBirthDate = v) } }
    fun onRegGenderChanged(v: String) { _uiState.update { it.copy(regGender = v) } }
    fun onRegGroupChanged(id: Int) { _uiState.update { it.copy(regGroupId = id) } }

    // --- Действия ---

    // Вход в систему
    fun login() {
        val state = _uiState.value
        if (state.loginText.isBlank() || state.passwordText.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Заполните все поля") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.login(state.loginText, state.passwordText)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                loadUsers() // загружаем список пользователей сразу после входа
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    // Регистрация
    fun register() {
        val state = _uiState.value
        if (state.regLogin.isBlank() || state.regPassword.isBlank() ||
            state.regEmail.isBlank() || state.regFirstName.isBlank() ||
            state.regLastName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Заполните обязательные поля") }
            return
        }

        val person = PersonDto(
            firstName = state.regFirstName,
            lastName = state.regLastName,
            middleName = state.regMiddleName.ifBlank { null },
            birthDate = state.regBirthDate.ifBlank { null },
            gender = state.regGender,
            groupId = if (state.regGroupId > 0) state.regGroupId else null
        )

        val request = RegisterRequest(
            login = state.regLogin,
            password = state.regPassword,
            email = state.regEmail,
            phoneNumber = state.regPhone,
            person = person
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.register(request).onSuccess {
                _uiState.update { it.copy(isLoading = false, isRegistered = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    // Загрузить список пользователей
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getUsers().onSuccess { users ->
                _uiState.update { it.copy(isLoading = false, users = users) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    // Загрузить группы (для регистрации)
    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups().onSuccess { groups ->
                _uiState.update { it.copy(groups = groups) }
            }
        }
    }

    // Выход из аккаунта
    fun logout() {
        TokenManager.clear()
        _uiState.update { AuthUiState() } // сбрасываем всё
    }

    // Сбросить флаг регистрации (для возврата на логин)
    fun clearRegistered() {
        _uiState.update { it.copy(isRegistered = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
