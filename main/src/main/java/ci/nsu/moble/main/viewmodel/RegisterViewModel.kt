package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.dto.GroupDto
import ci.nsu.moble.main.data.dto.PersonDto
import ci.nsu.moble.main.data.dto.RegisterRequestDto
import ci.nsu.moble.main.data.dto.UserDto
import ci.nsu.moble.main.data.repositories.AuthRepository
import ci.nsu.moble.main.viewmodel.states.AuthState
import ci.nsu.moble.main.viewmodel.states.GroupsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager,
    private val savedState: SavedStateHandle
) : ViewModel() {

    // В ViewModel
    val login: MutableStateFlow<String> = savedState.getMutableStateFlow("login_key", "user_login")
    val password = savedState.getMutableStateFlow("password", "securePassword123")
    val email = savedState.getMutableStateFlow("email", "test@example.com")
    val phone = savedState.getMutableStateFlow("phone", "+1234567890")
    val firstName = savedState.getMutableStateFlow("firstName", "Иван")
    val lastName = savedState.getMutableStateFlow("lastName", "Иванов")
    val middleName = savedState.getMutableStateFlow("middleName", "Иванович")
    val birthDate = savedState.getMutableStateFlow("birthDate", "2000-01-31")
    val gender = savedState.getMutableStateFlow("gender", "MALE")
    val selectedGroup = savedState.getMutableStateFlow<GroupDto?>( "selectedGroup", null)

    fun <T> updateField(key: String, value: T, flow: MutableStateFlow<T>) {
        savedState[key] = value
        flow.value = value
    }

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users

    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups

    private val _groupsState = MutableStateFlow<GroupsState>(GroupsState.Idle)
    val groupsState: StateFlow<GroupsState> = _groupsState

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _groupsState.value = GroupsState.Loading
            repository.getGroups()
                .onSuccess {
                    _groupsState.value = GroupsState.Success(it)
                }
                .onFailure {
                    _groupsState.value = GroupsState.Error(it.message ?: "Unknown error")
                }
        }
    }

    fun register(
        login: String, pass: String, email: String, phone: String,
        fName: String, lName: String, mName: String, bDate: String, gender: String, gId: Int,
        onSuccess: () -> Unit
    ) {
        // Создаем вложенный объект персоны
        val person = PersonDto(
            firstName = fName,
            lastName = lName,
            middleName = mName,
            birthDate = bDate, // Убедись, что строка в формате "2000-01-31"
            gender = gender,   // Должно быть строго "MALE" или "FEMALE"
            groupId = gId
        )

        // Создаем основной запрос
        val request = RegisterRequestDto(
            login = login,
            password = pass,
            email = email,
            phoneNumber = phone,
            person = person
        )

        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            repository.register(request)
                .onSuccess {
                    _state.value = AuthState(isLoading = false)
                    onSuccess()
                }
                .onFailure { error ->
                    _state.value = AuthState(error = "Ошибка: ${error.message}", isLoading = false)
                }
        }
    }
}