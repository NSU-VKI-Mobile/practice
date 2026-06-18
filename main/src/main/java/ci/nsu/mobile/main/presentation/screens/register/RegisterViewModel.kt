package ci.nsu.mobile.main.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.remote.dto.GroupDto
import ci.nsu.mobile.main.data.remote.dto.PersonDto
import ci.nsu.mobile.main.data.remote.dto.RegisterRequest
import ci.nsu.mobile.main.domain.repository.AuthRepository
import ci.nsu.mobile.main.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingGroups = true) }
            when (val result = repository.getGroups()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            groups = result.data,
                            isLoadingGroups = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoadingGroups = false
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun updateFirstName(value: String) = _uiState.update { it.copy(firstName = value) }
    fun updateLastName(value: String) = _uiState.update { it.copy(lastName = value) }
    fun updateMiddleName(value: String) = _uiState.update { it.copy(middleName = value) }
    fun updateBirthDate(value: String) = _uiState.update { it.copy(birthDate = value) }
    fun updateGender(value: String) = _uiState.update { it.copy(gender = value) }
    fun updateGroupId(value: Int) = _uiState.update { it.copy(groupId = value) }
    fun updateLogin(value: String) = _uiState.update { it.copy(login = value) }
    fun updatePassword(value: String) = _uiState.update { it.copy(password = value) }
    fun updateEmail(value: String) = _uiState.update { it.copy(email = value) }
    fun updatePhone(value: String) = _uiState.update { it.copy(phone = value) }

    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.firstName.isBlank() || state.lastName.isBlank() ||
            state.login.isBlank() || state.password.isBlank() ||
            state.email.isBlank() || state.groupId == 0) {
            _uiState.update { it.copy(error = "Заполните все обязательные поля") }
            return
        }

        val person = PersonDto(
            firstName = state.firstName,
            lastName = state.lastName,
            middleName = state.middleName,
            birthDate = state.birthDate,
            gender = state.gender,
            groupId = state.groupId
        )

        val request = RegisterRequest(
            login = state.login,
            password = state.password,
            email = state.email,
            phoneNumber = state.phone,
            person = person
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = repository.register(request)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String = "",
    val gender: String = "MALE",
    val groupId: Int = 0,
    val groups: List<GroupDto> = emptyList(),
    val isLoadingGroups: Boolean = false,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)