package ci.nsu.mobile.main.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

class RegistrationViewModel : ViewModel() {
    private val repository = AuthRepository()

    @OptIn(InternalSerializationApi::class)
    var groups by mutableStateOf<List<GroupDto>>(emptyList())
    var selectedGroupId by mutableStateOf<Int?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var registrationSuccess by mutableStateOf(false)

    // Person fields
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var middleName by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var gender by mutableStateOf("")

    // Account fields
    var login by mutableStateOf("")
    var password by mutableStateOf("")
    var email by mutableStateOf("")
    var phoneNumber by mutableStateOf("")

    init {
        loadGroups()
    }

    @OptIn(InternalSerializationApi::class)
    private fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groups = it }
                .onFailure { errorMessage = it.message }
        }
    }

    @OptIn(InternalSerializationApi::class)
    fun performRegistration() {
        val groupId = selectedGroupId
        if (groupId == null) {
            errorMessage = "Выберите группу"
            return
        }
        if (firstName.isBlank() || lastName.isBlank() || login.isBlank() || password.isBlank()) {
            errorMessage = "Заполните обязательные поля"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val person = PersonDto(firstName, lastName, middleName, birthDate, gender, groupId)
            val request = RegisterRequest(login, password, email, phoneNumber, person = person)
            repository.register(request)
                .onSuccess { registrationSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}