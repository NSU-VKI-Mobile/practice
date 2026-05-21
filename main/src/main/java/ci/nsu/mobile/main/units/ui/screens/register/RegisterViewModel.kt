package ci.nsu.mobile.main.units.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.units.data.model.GroupDto
import ci.nsu.mobile.main.units.data.model.PersonDto
import ci.nsu.mobile.main.units.data.model.RegisterRequest
import ci.nsu.mobile.main.units.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel(){
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var middleName by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var gender by mutableStateOf("")
    var login by mutableStateOf("")
    var password by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var selectedGroupId by mutableStateOf<Int?>(null)

    var groups by mutableStateOf<List<GroupDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var registrationSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadGroups()
    }
    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groups = it }
                .onFailure { errorMessage = "Ошибка при загрузке групп" }
        }
    }
    fun register() {
        val person = PersonDto(
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            birthDate = birthDate,
            gender = gender,
            groupId = selectedGroupId?:0
        )
        val request = RegisterRequest(
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
                .onSuccess { registrationSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}