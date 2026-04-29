package ci.nsu.mobile.main.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.domain.repository.AuthRepository
import ci.nsu.mobile.main.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private var groups: List<GroupDto> = emptyList()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            when (val result = repository.getGroups()) {
                is Result.Success -> {
                    groups = result.data
                    _uiState.value = _uiState.value.copy(groups = groups)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = "Ошибка загрузки групп"
                    )
                }
            }
        }
    }

    // Валидация всех полей
    fun validateFields(
        firstName: String,
        lastName: String,
        birthDate: String,
        login: String,
        password: String,
        email: String,
        phoneNumber: String,
        groupSelected: Boolean
    ): String? {
        if (firstName.isBlank()) return "Введите имя"
        if (firstName.length < 2) return "Имя слишком короткое"

        if (lastName.isBlank()) return "Введите фамилию"
        if (lastName.length < 2) return "Фамилия слишком короткая"

        if (birthDate.isBlank()) return "Введите дату рождения"
        if (!birthDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
            return "Дата в формате YYYY-MM-DD (пример: 2000-01-25)"

        if (!groupSelected) return "Выберите группу"

        if (login.isBlank()) return "Введите логин"
        if (login.length < 3) return "Логин минимум 3 символа"

        if (password.isBlank()) return "Введите пароль"
        if (password.length < 6) return "Пароль минимум 6 символов"

        if (email.isBlank()) return "Введите email"
        if (!email.contains("@") || !email.contains("."))
            return "Введите корректный email"

        if (phoneNumber.isBlank()) return "Введите номер телефона"
        if (!phoneNumber.matches(Regex("\\+?[0-9]{10,12}")))
            return "Телефон должен содержать 10-12 цифр"

        return null
    }

    fun setError(message: String){
        _uiState.value=_uiState.value.copy(error=message)
    }
    fun register(
        firstName: String,
        lastName: String,
        middleName: String?,
        birthDate: String,
        gender: String,
        groupId: Int,
        login: String,
        password: String,
        email: String,
        phoneNumber: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

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

            when (val result = repository.register(request)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    onSuccess()
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Ошибка регистрации"
                    )
                }
            }
        }
    }
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val groups: List<GroupDto> = emptyList()
)
