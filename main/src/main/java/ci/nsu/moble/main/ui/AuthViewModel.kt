package ci.nsu.moble.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.AuthRepository
import ci.nsu.moble.main.data.dto.GroupDto
import ci.nsu.moble.main.data.dto.PersonDto
import ci.nsu.moble.main.data.dto.RegisterRequest
import ci.nsu.moble.main.data.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val groups: List<GroupDto> = emptyList()
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(error = "Введите логин и пароль")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result: Result<UserDto?> = repository.login(login, password)

            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
                loadUsers()
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка входа"
                )
            }
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        birthDate: String,
        gender: String,
        groupId: Int?,
        login: String,
        password: String,
        email: String,
        phoneNumber: String,
        onSuccess: () -> Unit
    ) {
        if (
            firstName.isBlank() ||
            lastName.isBlank() ||
            birthDate.isBlank() ||
            gender.isBlank() ||
            groupId == null ||
            login.isBlank() ||
            password.isBlank() ||
            email.isBlank() ||
            phoneNumber.isBlank()
        ) {
            _state.value = _state.value.copy(error = "Заполните все обязательные поля")
            return
        }

        val person = PersonDto(
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            birthDate = birthDate,
            gender = gender,
            groupId = groupId
        )

        val request = RegisterRequest(
            login = login,
            password = password,
            email = email,
            phoneNumber = phoneNumber,
            roleId = 1,
            authAllowed = true,
            person = person
        )

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result: Result<Unit> = repository.register(request)

            if (result.isSuccess) {
                _state.value = _state.value.copy(isLoading = false)
                onSuccess()
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка регистрации"
                )
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result: Result<List<UserDto>> = repository.getUsers()

            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    users = result.getOrNull() ?: emptyList()
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка загрузки пользователей"
                )
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            val result: Result<List<GroupDto>> = repository.getGroups()

            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    groups = result.getOrNull() ?: emptyList()
                )
            } else {
                _state.value = _state.value.copy(
                    error = result.exceptionOrNull()?.message ?: "Ошибка загрузки групп"
                )
            }
        }
    }

    fun logout() {
        repository.logout()
        _state.value = AuthUiState(isLoggedIn = false)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}