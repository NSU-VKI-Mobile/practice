package ci.nsu.mobile.main.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegistrationRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var middleName by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var gender by mutableStateOf("Male")

    var login by mutableStateOf("")
    var password by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")

    var groups by mutableStateOf<List<GroupDto>>(emptyList())
    var selectedGroup by mutableStateOf<GroupDto?>(null)

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        loadGroups()
    }

    private fun loadGroups() {

        viewModelScope.launch {

            repository.getGroups()
                .onSuccess {
                    groups = it
                }
                .onFailure {
                    error = it.message
                }
        }
    }

    fun register(
        onSuccess: () -> Unit
    ) {

        val person = PersonDto(
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            birthDate = birthDate,
            gender = gender,
            groupId = selectedGroup?.id ?: 0
        )

        val request = RegistrationRequest(
            login = login,
            password = password,
            email = email,
            phoneNumber = phone,
            roleId = 1,
            authAllowed = true,
            person = person
        )

        viewModelScope.launch {

            isLoading = true

            repository.register(request)
                .onSuccess {
                    onSuccess()
                }
                .onFailure {
                    error = it.message
                }

            isLoading = false
        }
    }
}