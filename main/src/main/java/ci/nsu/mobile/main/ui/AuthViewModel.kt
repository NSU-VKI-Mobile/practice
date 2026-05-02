package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.network.ApiConfig
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
    val gender: String = "",
    val group: GroupDto? = null,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)

data class AuthUiState(
    val screen: AuthScreen = AuthScreen.Login,
    val currentUserId: Long? = null,
    val login: String = "",
    val password: String = "",
    val registerForm: RegisterForm = RegisterForm(),
    val groups: List<GroupDto> = emptyList(),
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AuthUiState(
            screen = if (TokenManager.token.isNullOrBlank()) AuthScreen.Login else AuthScreen.Users,
            currentUserId = TokenManager.userId
        )
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        if (!TokenManager.token.isNullOrBlank()) {
            loadUsers()
        }
    }

    fun onLoginChanged(value: String) {
        _uiState.update { it.copy(login = value, errorMessage = null, infoMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null, infoMessage = null) }
    }

    fun onRegisterFormChanged(form: RegisterForm) {
        _uiState.update { it.copy(registerForm = form, errorMessage = null, infoMessage = null) }
    }

    fun openRegister() {
        _uiState.update {
            it.copy(
                screen = AuthScreen.Register,
                errorMessage = null,
                infoMessage = null
            )
        }
        if (_uiState.value.groups.isEmpty()) {
            loadGroups()
        }
    }

    fun openLogin() {
        _uiState.update {
            it.copy(
                screen = AuthScreen.Login,
                errorMessage = null,
                infoMessage = null
            )
        }
    }

    fun login() {
        val currentState = _uiState.value
        val validationError = validateLogin(currentState.login, currentState.password)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError, infoMessage = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }

            repository.login(
                login = currentState.login.trim(),
                password = currentState.password
            ).onSuccess { user ->
                _uiState.update {
                    it.copy(
                        screen = AuthScreen.Users,
                        currentUserId = TokenManager.userId,
                        users = if (user.login.isBlank() && user.person == null) it.users else listOf(user),
                        isLoading = false,
                        password = "",
                        errorMessage = null
                    )
                }
                loadUsers()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toUserMessage(),
                        infoMessage = null
                    )
                }
            }
        }
    }

    fun register() {
        val form = _uiState.value.registerForm
        val validationError = validateRegistration(form)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError, infoMessage = null) }
            return
        }

        val selectedGroup = requireNotNull(form.group)
        val request = RegisterRequest(
            login = form.login.trim(),
            password = form.password,
            email = form.email.trim(),
            phoneNumber = form.phoneNumber.trim(),
            roleId = 1,
            authAllowed = true,
            person = PersonDto(
                firstName = form.firstName.trim(),
                lastName = form.lastName.trim(),
                middleName = form.middleName.trim(),
                birthDate = form.birthDate.trim(),
                gender = form.gender.trim(),
                groupId = selectedGroup.id
            )
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }

            repository.register(request)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            screen = AuthScreen.Login,
                            registerForm = RegisterForm(),
                            isLoading = false,
                            errorMessage = null,
                            infoMessage = "Регистрация выполнена. Теперь можно войти."
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUserMessage(),
                            infoMessage = null
                        )
                    }
                }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getGroups()
                .onSuccess { groups ->
                    _uiState.update { state ->
                        val currentGroup = state.registerForm.group
                        val selectedGroup = currentGroup?.takeIf { group ->
                            groups.any { it.id == group.id }
                        } ?: groups.firstOrNull()

                        state.copy(
                            groups = groups,
                            registerForm = state.registerForm.copy(group = selectedGroup),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUserMessage()
                        )
                    }
                }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.getUsers()
                .onSuccess { users ->
                    _uiState.update {
                        it.copy(
                            screen = AuthScreen.Users,
                            users = users,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUserMessage()
                        )
                    }
                }
        }
    }

    fun logout() {
        TokenManager.clear()
        _uiState.update {
            AuthUiState(
                screen = AuthScreen.Login,
                currentUserId = null,
                login = it.login
            )
        }
    }

    private fun validateLogin(login: String, password: String): String? {
        return when {
            login.isBlank() -> "Введите логин."
            password.isBlank() -> "Введите пароль."
            else -> null
        }
    }

    private fun validateRegistration(form: RegisterForm): String? {
        return when {
            form.firstName.isBlank() -> "Введите имя."
            form.lastName.isBlank() -> "Введите фамилию."
            form.birthDate.isBlank() -> "Введите дату рождения."
            form.gender.isBlank() -> "Введите пол."
            form.group == null -> "Выберите группу."
            form.login.isBlank() -> "Введите логин."
            form.password.length < 4 -> "Пароль должен содержать минимум 4 символа."
            form.email.isBlank() || !form.email.contains("@") -> "Введите корректный email."
            form.phoneNumber.isBlank() -> "Введите телефон."
            else -> null
        }
    }

    private fun Throwable.toUserMessage(): String {
        return when (this) {
            is HttpException -> when (code()) {
                400 -> "Проверьте введённые данные."
                401 -> "Неверный логин или пароль."
                403 -> "Нет доступа. Авторизуйтесь снова."
                404 -> "Метод API не найден."
                in 500..599 -> "Сервер временно недоступен."
                else -> "Ошибка сервера: HTTP ${code()}."
            }
            is IOException -> "Не удалось подключиться к серверу: ${ApiConfig.baseUrl}"
            is IllegalStateException -> message ?: "Ошибка авторизации."
            else -> localizedMessage ?: "Произошла неизвестная ошибка."
        }
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
