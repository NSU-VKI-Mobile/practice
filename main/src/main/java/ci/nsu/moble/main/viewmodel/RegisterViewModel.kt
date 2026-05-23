package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.dto.GroupDto
import ci.nsu.moble.main.data.dto.PersonDto
import ci.nsu.moble.main.data.dto.RegisterRequestDto
import ci.nsu.moble.main.data.repositories.AuthRepository
import ci.nsu.moble.main.viewmodel.states.GroupsState
import ci.nsu.moble.main.viewmodel.states.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    init {
        restoreState() // restore form SavedState
        loadGroups()
    }

    fun <T> updateField(key: String, value: T, updateAction: (RegisterUiState) -> RegisterUiState) {
        savedState[key] = value
        _uiState.update { updateAction(it) }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(groupsState = GroupsState.Loading) }
            repository.getGroups()
                .onSuccess { groups ->
                    _uiState.update { it.copy(groupsState = GroupsState.Success(groups)) }
                }
                .onFailure { error ->
                    val msg = error.message ?: "Unknown error"
                    _uiState.update { it.copy(groupsState = GroupsState.Error(msg)) }
                }
        }
    }

    fun register(onSuccess: () -> Unit) {
        val current = _uiState.value
        val gId = current.selectedGroup?.id ?: return

        val person = PersonDto(
            firstName = current.firstName,
            lastName = current.lastName,
            middleName = current.middleName,
            birthDate = current.birthDate,
            gender = current.gender,
            groupId = gId
        )

        val request = RegisterRequestDto(
            login = current.login,
            password = current.password,
            email = current.email,
            phoneNumber = current.phone,
            person = person
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.register(request)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = "Ошибка: ${error.message}", isLoading = false) }
                }
        }
    }

    private fun restoreState() {
        _uiState.update {
            RegisterUiState(
                login = savedState.get<String>("login_key") ?: "user_login",
                password = savedState.get<String>("password") ?: "securePassword123",
                email = savedState.get<String>("email") ?: "test@example.com",
                phone = savedState.get<String>("phone") ?: "+1234567890",
                firstName = savedState.get<String>("firstName") ?: "Иван",
                lastName = savedState.get<String>("lastName") ?: "Иванов",
                middleName = savedState.get<String>("middleName") ?: "Иванович",
                birthDate = savedState.get<String>("birthDate") ?: "2000-01-31",
                gender = savedState.get<String>("gender") ?: "MALE",
                selectedGroup = savedState.get<GroupDto>("selectedGroup")
            )
        }
    }
}