package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val availableGroups: List<GroupDto> = emptyList()
)

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    availableGroups = result.getOrNull() ?: emptyList()
                )
            }
        }
    }

    fun register(
        login: String,
        password: String,
        email: String,
        phoneNumber: String,
        firstName: String,
        lastName: String,
        middleName: String?,
        birthDate: String,
        gender: String,
        groupId: Int
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

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
                person = person
            )

            val result = repository.register(request)

            _uiState.value = if (result.isSuccess) {
                RegisterUiState(isSuccess = true)
            } else {
                RegisterUiState(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Registration failed"
                )
            }
        }
    }

    fun resetSuccessState() {
        _uiState.value = RegisterUiState()
    }
}