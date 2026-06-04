package com.example.integratedapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integratedapp.data.SessionManager
import com.example.integratedapp.data.model.*
import com.example.integratedapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthUiState(
    // Поля логина
    val loginText: String = "",
    val passwordText: String = "",

    // Поля регистрации
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

    // Состояние
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = SessionManager.isLoggedIn(),
    val isRegistered: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onLoginChanged(v: String) { _uiState.update { it.copy(loginText = v, errorMessage = null) } }
    fun onPasswordChanged(v: String) { _uiState.update { it.copy(passwordText = v, errorMessage = null) } }

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

    fun login() {
        val state = _uiState.value
        if (state.loginText.isBlank() || state.passwordText.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Заполните все поля") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.login(state.loginText, state.passwordText).onSuccess {
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

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
            login = state.regLogin, password = state.regPassword,
            email = state.regEmail, phoneNumber = state.regPhone, person = person
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

    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups().onSuccess { groups ->
                _uiState.update { it.copy(groups = groups) }
            }
        }
    }

    fun logout() {
        SessionManager.clear()
        _uiState.update { AuthUiState() }
    }

    fun clearRegistered() {
        _uiState.update { it.copy(isRegistered = false) }
    }
}
