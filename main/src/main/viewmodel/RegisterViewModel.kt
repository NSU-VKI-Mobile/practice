package com.example.userapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userapp.data.models.PersonDto
import com.example.userapp.data.models.RegisterRequest
import com.example.userapp.repository.AuthRepository
import com.example.userapp.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    private val _groupsState = MutableStateFlow<GroupsUiState>(GroupsUiState())
    val groupsState: StateFlow<GroupsUiState> = _groupsState.asStateFlow()

    fun loadGroups() {
        viewModelScope.launch {
            _groupsState.value = GroupsUiState(isLoading = true)

            val result = repository.getGroups()

            when (result) {
                is Result.Success -> {
                    _groupsState.value = GroupsUiState(
                        groups = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _groupsState.value = GroupsUiState(
                        error = result.message,
                        isLoading = false
                    )
                }
                else -> {}
            }
        }
    }

    fun updateField(field: String, value: String) {
        val current = _registerState.value
        when (field) {
            "firstName" -> _registerState.value = current.copy(firstName = value, firstNameError = null)
            "lastName" -> _registerState.value = current.copy(lastName = value, lastNameError = null)
            "middleName" -> _registerState.value = current.copy(middleName = value)
            "birthDate" -> _registerState.value = current.copy(birthDate = value, birthDateError = null)
            "gender" -> _registerState.value = current.copy(gender = value, genderError = null)
            "login" -> _registerState.value = current.copy(login = value, loginError = null)
            "password" -> _registerState.value = current.copy(password = value, passwordError = null)
            "email" -> _registerState.value = current.copy(email = value, emailError = null)
            "phoneNumber" -> _registerState.value = current.copy(phoneNumber = value)
        }
    }

    fun selectGroup(groupId: Int) {
        _registerState.value = _registerState.value.copy(groupId = groupId, groupError = null)
    }

    fun selectGender(gender: String) {
        _registerState.value = _registerState.value.copy(gender = gender, genderError = null)
    }

    fun register() {
        val state = _registerState.value

        var hasError = false

        if (state.firstName.isEmpty()) {
            _registerState.value = state.copy(firstNameError = "Введите имя")
            hasError = true
        }

        if (state.lastName.isEmpty()) {
            _registerState.value = state.copy(lastNameError = "Введите фамилию")
            hasError = true
        }

        if (state.birthDate.isEmpty()) {
            _registerState.value = state.copy(birthDateError = "Выберите дату рождения")
            hasError = true
        }

        if (state.gender.isEmpty()) {
            _registerState.value = state.copy(genderError = "Выберите пол")
            hasError = true
        }

        if (state.groupId == null) {
            _registerState.value = state.copy(groupError = "Выберите группу")
            hasError = true
        }

        if (state.login.isEmpty()) {
            _registerState.value = state.copy(loginError = "Введите логин")
            hasError = true
        }

        if (state.password.isEmpty()) {
            _registerState.value = state.copy(passwordError = "Введите пароль")
            hasError = true
        }

        if (state.email.isEmpty()) {
            _registerState.value = state.copy(emailError = "Введите email")
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _registerState.value = state.copy(emailError = "Введите корректный email")
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _registerState.value = state.copy(isLoading = true, error = null)

            val person = PersonDto(
                firstName = state.firstName,
                lastName = state.lastName,
                middleName = state.middleName.ifEmpty { null },
                birthDate = state.birthDate,
                gender = state.gender,
                groupId = state.groupId!!
            )

            val request = RegisterRequest(
                login = state.login,
                password = state.password,
                email = state.email,
                phoneNumber = state.phoneNumber.ifEmpty { null },
                roleId = 1,
                authAllowed = true,
                person = person
            )

            val result = repository.register(request)

            when (result) {
                is Result.Success -> {
                    _registerState.value = state.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                is Result.Error -> {
                    _registerState.value = state.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {}
            }
        }
    }

    fun resetSuccess() {
        _registerState.value = _registerState.value.copy(isSuccess = false)
    }

    fun resetError() {
        _registerState.value = _registerState.value.copy(error = null)
    }

    data class RegisterUiState(
        // Person fields
        val firstName: String = "",
        val lastName: String = "",
        val middleName: String = "",
        val birthDate: String = "",
        val gender: String = "",
        val groupId: Int? = null,
        // User fields
        val login: String = "",
        val password: String = "",
        val email: String = "",
        val phoneNumber: String = "",
        // Errors
        val firstNameError: String? = null,
        val lastNameError: String? = null,
        val birthDateError: String? = null,
        val genderError: String? = null,
        val groupError: String? = null,
        val loginError: String? = null,
        val passwordError: String? = null,
        val emailError: String? = null,
        // State
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSuccess: Boolean = false
    )

    data class GroupsUiState(
        val groups: List<com.example.userapp.data.models.GroupDto> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}