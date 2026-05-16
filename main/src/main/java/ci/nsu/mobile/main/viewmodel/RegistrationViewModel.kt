package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.network.model.PersonDto
import ci.nsu.mobile.main.data.network.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.viewmodel.state.RegisterEvents
import ci.nsu.mobile.main.viewmodel.state.RegisterState
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
                _state.update { it.copy(lastName = event.newSurname) }
            }

            is RegisterEvents.NameChanged -> {
                _state.update { it.copy(firstName = event.newName) }
            }

            is RegisterEvents.PatronymicChanged -> {
                _state.update { it.copy(middleName = event.newPatronymic) }
            }

            is RegisterEvents.BirthdayChanged -> {
                _state.update { it.copy(birthDate = event.newDate) }
            }

            is RegisterEvents.EmailChanged -> {
                _state.update { it.copy(email = event.newEmail) }
            }

            is RegisterEvents.GenderChanged -> {
                _state.update { it.copy(gender = event.newGender) }
            }

            is RegisterEvents.GroupChanged -> {
                _state.update { currentState ->
                    val selectedGroup = currentState.groups.find { it.groupId == event.newGroup }
                    currentState.copy(
                        groupId = event.newGroup,
                        groupName = selectedGroup?.groupName ?: ""
                    )
                }
            }

            is RegisterEvents.LoginChanged -> {
                _state.update { it.copy(login = event.newLogin) }
            }

            is RegisterEvents.PasswordChanged -> {
                _state.update { it.copy(password = event.newPassword) }
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
        viewModelScope.launch {
            val result = repository.getGroups()
            result.onSuccess { groups ->
                _state.update { it.copy(groups = groups) }
            }.onFailure { error ->
                _state.update { it.copy(errorMessage = "Ошибка загрузки групп: ${error.message}") }
            }
        }
    }
    private fun register() {
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
                _state.update { it.copy(isSuccess = true, errorMessage = null) }
            }
            if (result.isFailure) {
                _state.update { it.copy(isSuccess = false, errorMessage = "error register") }
            }
        }
    }

    private fun validation(): Boolean {
        val stateValue = _state.value

        _state.update { it.copy(errorMessage = null) }

        return when {
            stateValue.lastName.isEmpty() -> {
                _state.update { it.copy(errorMessage = "Заполните поле фамилия") }
                false
            }
            stateValue.firstName.isEmpty() -> {
                _state.update { it.copy(errorMessage = "Заполните поле имя") }
                false
            }
            stateValue.groupId == 0 -> {
                _state.update { it.copy(errorMessage = "Выберите группу") }
                false
            }
            stateValue.login.isEmpty() -> {
                _state.update { it.copy(errorMessage = "Заполните поле логин") }
                false
            }
            stateValue.password.isEmpty() -> {
                _state.update { it.copy(errorMessage = "Заполните поле пароль") }
                false
            }
            stateValue.email.isEmpty() -> {
                _state.update { it.copy(errorMessage = "Заполните поле почта") }
                false
            }
            else -> {
                _state.update { it.copy(errorMessage = null) }
                true
            }
        }
    }

    private fun cleanAll() {
        _state.update { RegisterState() }
    }
}