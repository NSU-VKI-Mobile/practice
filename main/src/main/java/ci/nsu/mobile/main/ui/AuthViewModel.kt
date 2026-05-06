package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.network.GroupDto
import ci.nsu.mobile.main.data.network.PersonDto
import ci.nsu.mobile.main.data.network.RegisterRequest
import ci.nsu.mobile.main.data.network.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthScreen {
    Login,
    Register,
    Users
}

data class RegisterForm(
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String = "",
    val gender: String = "MALE",
    val groupId: Int? = null,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)

data class AuthUiState(
    val screen: AuthScreen = AuthScreen.Login,
    val login: String = "",
    val password: String = "",
    val registerForm: RegisterForm = RegisterForm(),
    val groups: List<GroupDto> = emptyList(),
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onLoginChanged(value: String) {
        _uiState.update { it.copy(login = value, message = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, message = null) }
    }

    fun updateRegisterForm(transform: (RegisterForm) -> RegisterForm) {
        _uiState.update { it.copy(registerForm = transform(it.registerForm), message = null) }
    }

    fun openRegister() {
        _uiState.update { it.copy(screen = AuthScreen.Register, message = null) }
        loadGroups()
    }

    fun openLogin() {
        _uiState.update { it.copy(screen = AuthScreen.Login, message = null) }
    }

    fun login() {
        val state = _uiState.value
        if (state.login.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(message = "Введите логин и пароль") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            repository.login(state.login, state.password)
                .onSuccess {
                    _uiState.update { it.copy(screen = AuthScreen.Users) }
                    loadUsers()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(message = error.message ?: "Ошибка входа") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun register() {
        val form = _uiState.value.registerForm
        if (!form.isValid()) {
            _uiState.update { it.copy(message = "Заполните обязательные поля регистрации") }
            return
        }

        val request = RegisterRequest(
            login = form.login,
            password = form.password,
            email = form.email,
            phoneNumber = form.phoneNumber,
            person = PersonDto(
                firstName = form.firstName,
                lastName = form.lastName,
                middleName = form.middleName,
                birthDate = form.birthDate,
                gender = form.gender,
                groupId = form.groupId ?: 0
            )
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            repository.register(request)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            screen = AuthScreen.Login,
                            message = "Регистрация выполнена. Теперь войдите.",
                            login = form.login,
                            password = ""
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(message = error.message ?: "Ошибка регистрации") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            repository.getUsers()
                .onSuccess { users ->
                    _uiState.update { it.copy(users = users, screen = AuthScreen.Users) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(message = error.message ?: "Ошибка загрузки") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.update { AuthUiState(message = "Вы вышли из аккаунта") }
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            repository.getGroups()
                .onSuccess { groups ->
                    _uiState.update {
                        it.copy(
                            groups = groups,
                            registerForm = it.registerForm.copy(
                                groupId = it.registerForm.groupId ?: groups.firstOrNull()?.id
                            )
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(message = error.message ?: "Ошибка загрузки групп") }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun RegisterForm.isValid(): Boolean =
        firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            birthDate.isNotBlank() &&
            login.isNotBlank() &&
            password.isNotBlank() &&
            email.isNotBlank() &&
            phoneNumber.isNotBlank() &&
            groupId != null
}
