package ci.nsu.mobile.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.model.GroupDto
import ci.nsu.mobile.auth.data.model.PersonDto
import ci.nsu.mobile.auth.data.model.RegisterRequest
import ci.nsu.mobile.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    init { loadGroups() }

    private fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(groups = result.getOrNull() ?: emptyList())
            }
        }
    }

    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(error = message)
    }

    fun validateFields(
        firstName: String, lastName: String, birthDate: String,
        login: String, password: String, email: String,
        phoneNumber: String, groupSelected: Boolean
    ): String? {
        if (firstName.isBlank()) return "Введите имя"
        if (lastName.isBlank()) return "Введите фамилию"
        if (birthDate.isBlank()) return "Введите дату рождения"
        if (!birthDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) return "Дата в формате YYYY-MM-DD"
        if (!groupSelected) return "Выберите группу"
        if (login.isBlank()) return "Введите логин"
        if (login.length < 3) return "Логин минимум 3 символа"
        if (password.isBlank()) return "Введите пароль"
        if (password.length < 6) return "Пароль минимум 6 символов"
        if (email.isBlank()) return "Введите email"
        if (!email.contains("@")) return "Введите корректный email"
        if (phoneNumber.isBlank()) return "Введите телефон"
        return null
    }

    fun register(
        firstName: String, lastName: String, middleName: String?,
        birthDate: String, gender: String, groupId: Int,
        login: String, password: String, email: String,
        phoneNumber: String, onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val person = PersonDto(firstName, lastName, middleName, birthDate, gender, groupId)
            val request = RegisterRequest(login, password, email, phoneNumber, person = person)
            val result = repository.register(request)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка регистрации"
                )
            }
        }
    }
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val groups: List<GroupDto> = emptyList()
)