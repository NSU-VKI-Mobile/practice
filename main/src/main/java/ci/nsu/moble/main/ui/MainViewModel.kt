// Task_6: ViewModel — навигация (3 экрана) + состояния + бизнес-логика.
package ci.nsu.moble.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.AuthRepository
import ci.nsu.moble.main.data.remote.TokenManager
import ci.nsu.moble.main.data.remote.dto.GroupDto
import ci.nsu.moble.main.data.remote.dto.PersonDto
import ci.nsu.moble.main.data.remote.dto.RegisterRequest
import ci.nsu.moble.main.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Screen {
    data object Login : Screen()
    data object Register : Screen()
    data object Users : Screen()
}

data class LoginState(
    val login: String = "",
    val password: String = "",
    val error: String? = null,
    val loading: Boolean = false
)

data class RegisterState(
    val lastName: String = "",
    val firstName: String = "",
    val patronymic: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val groups: List<GroupDto> = emptyList(),
    val selectedGroupId: Int? = null,
    val error: String? = null,
    val loading: Boolean = false,
    val success: Boolean = false
)

data class UsersState(
    val users: List<UserDto> = emptyList(),
    val error: String? = null,
    val loading: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Login)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private val _usersState = MutableStateFlow(UsersState())
    val usersState: StateFlow<UsersState> = _usersState.asStateFlow()

    init {
        TokenManager.init(application)
        if (TokenManager.token != null) {
            _currentScreen.value = Screen.Users
            loadUsers()
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        if (screen is Screen.Register) loadGroups()
        if (screen is Screen.Users) loadUsers()
    }

    // ---- Логин ----

    fun onLoginChanged(value: String) {
        _loginState.update { it.copy(login = value, error = null) }
    }

    fun onPasswordChanged(value: String) {
        _loginState.update { it.copy(password = value, error = null) }
    }

    fun onLoginClick() {
        val state = _loginState.value
        if (state.login.isBlank() || state.password.isBlank()) {
            _loginState.update { it.copy(error = "Заполните логин и пароль") }
            return
        }
        _loginState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val result = repository.login(state.login, state.password)
            result.fold(
                onSuccess = {
                    _loginState.update { it.copy(loading = false) }
                    _currentScreen.value = Screen.Users
                    loadUsers()
                },
                onFailure = { e ->
                    _loginState.update { it.copy(loading = false, error = e.message) }
                }
            )
        }
    }

    // ---- Регистрация ----

    fun onRegisterFieldChanged(
        lastName: String? = null,
        firstName: String? = null,
        patronymic: String? = null,
        birthDate: String? = null,
        gender: String? = null,
        login: String? = null,
        password: String? = null,
        email: String? = null,
        phoneNumber: String? = null
    ) {
        _registerState.update {
            it.copy(
                lastName = lastName ?: it.lastName,
                firstName = firstName ?: it.firstName,
                patronymic = patronymic ?: it.patronymic,
                birthDate = birthDate ?: it.birthDate,
                gender = gender ?: it.gender,
                login = login ?: it.login,
                password = password ?: it.password,
                email = email ?: it.email,
                phoneNumber = phoneNumber ?: it.phoneNumber,
                error = null
            )
        }
    }

    fun onGroupSelected(groupId: Int) {
        _registerState.update { it.copy(selectedGroupId = groupId) }
    }

    private fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            result.fold(
                onSuccess = { groups ->
                    _registerState.update {
                        it.copy(
                            groups = groups,
                            selectedGroupId = it.selectedGroupId ?: groups.firstOrNull()?.id
                        )
                    }
                },
                onFailure = { e ->
                    _registerState.update { it.copy(error = e.message) }
                }
            )
        }
    }

    fun onRegisterClick() {
        val s = _registerState.value
        if (s.lastName.isBlank() || s.firstName.isBlank() || s.login.isBlank() || s.password.isBlank()) {
            _registerState.update { it.copy(error = "Заполните обязательные поля") }
            return
        }
        _registerState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val person = PersonDto(
                firstName = s.firstName,
                lastName = s.lastName,
                middleName = s.patronymic,
                birthDate = s.birthDate,
                gender = s.gender,
                groupId = s.selectedGroupId ?: 0
            )
            val request = RegisterRequest(
                login = s.login,
                password = s.password,
                email = s.email,
                phoneNumber = s.phoneNumber,
                person = person
            )
            val result = repository.register(request)
            result.fold(
                onSuccess = {
                    _registerState.update { it.copy(loading = false, success = true) }
                },
                onFailure = { e ->
                    _registerState.update { it.copy(loading = false, error = e.message) }
                }
            )
        }
    }

    // ---- Пользователи ----

    private fun loadUsers() {
        _usersState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val result = repository.getUsers()
            result.fold(
                onSuccess = { users ->
                    _usersState.update { it.copy(users = users, loading = false) }
                },
                onFailure = { e ->
                    _usersState.update { it.copy(loading = false, error = e.message) }
                }
            )
        }
    }

    fun onLogoutClick() {
        repository.logout()
        _loginState.update { LoginState() }
        _usersState.update { UsersState() }
        _currentScreen.value = Screen.Login
    }
}
