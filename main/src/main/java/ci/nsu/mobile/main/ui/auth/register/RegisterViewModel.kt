package ci.nsu.mobile.main.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.dto.PersonDto
import ci.nsu.mobile.main.data.dto.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.GroupRepository
import ci.nsu.mobile.main.data.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(groupsLoading = true, groupsError = null) }

            val result = groupRepository.getGroups()

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            groups = result.data,
                            groupsLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            groupsError = result.message,
                            groupsLoading = false
                        )
                    }
                }
                is Result.Loading -> { }
            }
        }
    }

    // Form field updates
    fun updateFirstName(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(firstName = value, firstNameError = null))
        }
    }

    fun updateLastName(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(lastName = value, lastNameError = null))
        }
    }

    fun updateMiddleName(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(middleName = value))
        }
    }

    fun updateBirthDate(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(birthDate = value, birthDateError = null))
        }
    }

    fun updateGender(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(gender = value, genderError = null))
        }
    }

    fun updateGroupId(value: Int) {
        _uiState.update {
            it.copy(formState = it.formState.copy(groupId = value, groupError = null))
        }
    }

    fun updateLogin(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(login = value, loginError = null))
        }
    }

    fun updatePassword(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(password = value, passwordError = null))
        }
    }

    fun updateEmail(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(email = value, emailError = null))
        }
    }

    fun updatePhoneNumber(value: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(phoneNumber = value))
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        val form = _uiState.value.formState

        if (form.firstName.isBlank()) {
            _uiState.update { it.copy(formState = it.formState.copy(firstNameError = "Введите имя")) }
            isValid = false
        }

        if (form.lastName.isBlank()) {
            _uiState.update { it.copy(formState = it.formState.copy(lastNameError = "Введите фамилию")) }
            isValid = false
        }

        if (form.birthDate.isBlank()) {
            _uiState.update { it.copy(formState = it.formState.copy(birthDateError = "Введите дату рождения")) }
            isValid = false
        }

        if (form.gender.isBlank()) {
            _uiState.update { it.copy(formState = it.formState.copy(genderError = "Выберите пол")) }
            isValid = false
        }

        if (form.groupId == null) {
            _uiState.update { it.copy(formState = it.formState.copy(groupError = "Выберите группу")) }
            isValid = false
        }

        if (form.login.isBlank()) {
            _uiState.update { it.copy(formState = it.formState.copy(loginError = "Введите логин")) }
            isValid = false
        }

        if (form.password.isBlank()) {
            _uiState.update { it.copy(formState = it.formState.copy(passwordError = "Введите пароль")) }
            isValid = false
        } else if (form.password.length < 6) {
            _uiState.update { it.copy(formState = it.formState.copy(passwordError = "Пароль должен быть не менее 6 символов")) }
            isValid = false
        }

        if (form.email.isBlank()) {
            _uiState.update { it.copy(formState = it.formState.copy(emailError = "Введите email")) }
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(form.email).matches()) {
            _uiState.update { it.copy(formState = it.formState.copy(emailError = "Неверный формат email")) }
            isValid = false
        }

        return isValid
    }

    fun register(onSuccess: () -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            _uiState.update { it.copy(registerState = RegisterState.Loading) }

            val form = _uiState.value.formState

            val person = PersonDto(
                firstName = form.firstName,
                lastName = form.lastName,
                middleName = form.middleName.ifBlank { null },
                birthDate = form.birthDate,
                gender = form.gender.uppercase(),
                groupId = form.groupId!!
            )

            val request = RegisterRequest(
                login = form.login,
                password = form.password,
                email = form.email,
                phoneNumber = form.phoneNumber.ifBlank { null },
                roleId = 1,
                authAllowed = true,
                person = person
            )

            val result = authRepository.register(request)

            when (result) {
                is Result.Success -> {
                    if (!_uiState.value.hasNavigated) {
                        _uiState.update {
                            it.copy(
                                registerState = RegisterState.Success,
                                hasNavigated = true
                            )
                        }
                        onSuccess()
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(registerState = RegisterState.Error(result.message))
                    }
                }
                is Result.Loading -> { }
            }
        }
    }

    fun resetNavigation() {
        _uiState.update { it.copy(hasNavigated = false) }
    }
}