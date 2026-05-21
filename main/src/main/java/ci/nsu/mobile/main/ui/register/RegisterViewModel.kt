package ci.nsu.mobile.main.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val groups: List<GroupDto> = emptyList(),
    val isGroupsLoading: Boolean = false
)

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGroupsLoading = true)

            val result = repository.getGroups()
            result.fold(
                onSuccess = { groups ->
                    _uiState.value = _uiState.value.copy(
                        groups = groups,
                        isGroupsLoading = false
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        error = it.message,
                        isGroupsLoading = false
                    )
                }
            )
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        birthDate: String,
        gender: String,
        groupId: Int,
        login: String,
        password: String,
        email: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)

            val person = PersonDto(
                firstName = firstName,
                lastName = lastName,
                middleName = middleName,
                birthDate = birthDate,
                gender = gender,
                groupId = groupId
            )

            val registerRequest = RegisterRequest(
                login = login,
                password = password,
                email = email,
                phoneNumber = phoneNumber,
                roleId = 1,
                authAllowed = true,
                person = person
            )

            val result = repository.register(registerRequest)
            result.fold(
                onSuccess = {
                    _uiState.value = RegisterUiState(isSuccess = true)
                },
                onFailure = {
                    _uiState.value = RegisterUiState(error = it.message)
                }
            )
        }
    }
}