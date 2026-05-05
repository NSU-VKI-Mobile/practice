package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            AuthRepository.getGroups()
                .onSuccess { _groups.value = it }
                .onFailure { _error.value = it.message }
            _loading.value = false
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
        val validationError = validateInput(
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate,
            gender = gender,
            groupId = groupId,
            login = login,
            password = password,
            email = email,
            phoneNumber = phoneNumber
        )

        if (validationError != null) {
            _error.value = validationError
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val person = ci.nsu.mobile.main.data.model.PersonDto(
                firstName = firstName,
                lastName = lastName,
                middleName = middleName,
                birthDate = birthDate,
                gender = gender,
                groupId = groupId
            )

            val request = ci.nsu.mobile.main.data.model.RegisterRequest(
                login = login,
                password = password,
                email = email,
                phoneNumber = phoneNumber,
                roleId = 1,
                authAllowed = true,
                person = person
            )

            AuthRepository.register(request)
                .onSuccess { _success.value = true }
                .onFailure { _error.value = it.message }

            _loading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSuccess() {
        _success.value = false
    }

    private fun validateInput(
        firstName: String,
        lastName: String,
        birthDate: String,
        gender: String,
        groupId: Int,
        login: String,
        password: String,
        email: String,
        phoneNumber: String
    ): String? {
        if (firstName.isBlank() || lastName.isBlank()) {
            return "Имя и фамилия обязательны"
        }
        if (!birthDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            return "Дата рождения должна быть в формате YYYY-MM-DD"
        }
        if (gender.isBlank()) {
            return "Выберите пол"
        }
        if (groupId <= 0) {
            return "Выберите группу"
        }
        if (login.isBlank() || password.length < 4) {
            return "Введите логин и пароль не короче 4 символов"
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Введите корректный email"
        }
        if (phoneNumber.isBlank()) {
            return "Введите номер телефона"
        }
        return null
    }
}
