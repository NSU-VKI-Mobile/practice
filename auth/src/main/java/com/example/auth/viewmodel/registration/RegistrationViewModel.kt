package com.example.auth.viewmodel.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.data.network.model.PersonDto
import com.example.auth.data.network.model.RegisterRequest
import com.example.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    init {
        loadGroups()
    }

    fun registerEvent(event: RegisterEvents) {
        when (event) {
            is RegisterEvents.SurnameChanged -> {
                _state.update { it.copy(lastName = event.newSurname,
                    errorFields = it.errorFields - "LastName") }
            }

            is RegisterEvents.NameChanged -> {
                _state.update { it.copy(firstName = event.newName,
                    errorFields = it.errorFields - "FirstName") }
            }

            is RegisterEvents.PatronymicChanged -> {
                _state.update { it.copy(middleName = event.newPatronymic) }
            }

            is RegisterEvents.BirthdayChanged -> {
                _state.update { it.copy(birthDate = event.newDate) }
            }

            is RegisterEvents.EmailChanged -> {
                _state.update { it.copy(email = event.newEmail,
                    errorFields = it.errorFields - "Email") }
            }

            is RegisterEvents.GenderChanged -> {
                _state.update { it.copy(gender = event.newGender) }
            }

            is RegisterEvents.GroupChanged -> {
                _state.update { currentState ->
                    val selectedGroup = currentState.groups.find { it.groupId == event.newGroup }
                    currentState.copy(
                        groupId = event.newGroup,
                        groupName = selectedGroup?.groupName ?: "",
                    )
                }
            }

            is RegisterEvents.LoginChanged -> {
                _state.update { it.copy(login = event.newLogin,
                    errorFields = it.errorFields - "Login") }
            }

            is RegisterEvents.PasswordChanged -> {
                _state.update { it.copy(password = event.newPassword,
                    errorFields = it.errorFields - "Password") }
            }

            is RegisterEvents.PhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = event.newPhoneNumber) }
            }

            is RegisterEvents.MenuStateChanged -> {
                _state.update { it.copy(showDDMenu = event.newState) }
            }

            is RegisterEvents.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordState = event.newState) }
            }
            is RegisterEvents.DatePickerVisibilityChanged -> {
                _state.update { it.copy(showDatePicker = event.newState) }
            }
            RegisterEvents.SubmitRegister -> if (validation()) register()
            RegisterEvents.CleanAll -> cleanAll()
        }
    }

    private fun loadGroups() {
        _state.update { it.copy(isLoadingGroups = true) }
        viewModelScope.launch {
            val result = repository.getGroups()
            result.onSuccess { groups ->
                _state.update { it.copy(isLoadingGroups = false, groups = groups) }
            }.onFailure { error ->
                _state.update { it.copy(isLoadingGroups = false, errorMessage = "Ошибка загрузки групп: ${error.message}") }
            }
        }
    }
    private fun register() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val person = PersonDto(
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                middleName = _state.value.middleName,
                birthDate = _state.value.birthDate,
                gender = _state.value.gender,
                groupId = _state.value.groupId
            )
            val regReq = RegisterRequest(
                login = _state.value.login,
                password = _state.value.password,
                email = _state.value.email,
                phoneNumber = _state.value.phoneNumber,
                roleId = _state.value.roleId,
                person = person
            )
            val result = repository.register(regReq)
            if (result.isSuccess) {
                _state.update { it.copy(isSuccess = true, isLoading = false, errorMessage = null) }
            }
            if (result.isFailure) {
                _state.update { it.copy(isSuccess = false, isLoading = false, errorMessage = "Ошибка регистрации. Проверьте введенные данные") }
            }
        }
    }

    private fun validation(): Boolean {
        val stateValue = _state.value
        val errorFields = mutableSetOf<String>()

        if (stateValue.lastName.isEmpty()) errorFields.add("LastName")
        if (stateValue.firstName.isEmpty()) errorFields.add("FirstName")
        if (stateValue.groupId == -1) errorFields.add("Group")
        if (stateValue.login.isEmpty()) errorFields.add("Login")
        if (stateValue.password.isEmpty()) errorFields.add("Password")
        if (stateValue.email.isEmpty()) errorFields.add("Email")

        _state.update {
            it.copy(
                errorFields = errorFields,
                errorMessage = when {
                    errorFields.size > 1 -> "Заполните все обязательные поля"
                    errorFields.size == 1 -> "Заполните обязательное поле"
                    else -> null
                }
            )
        }
        return errorFields.isEmpty()
    }

    private fun cleanAll() {
        _state.update { RegisterState() }
    }
}