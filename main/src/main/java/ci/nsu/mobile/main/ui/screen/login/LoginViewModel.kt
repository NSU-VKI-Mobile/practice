package ci.nsu.mobile.main.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.domain.repository.AuthRepository
import ci.nsu.mobile.main.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    // Валидация полей входа
    fun validateFields(
        login: String,
        password: String
    ): String? {
        if (login.isBlank()) return "Введите логин"
        if (login.length < 3) return "Логин минимум 3 символа"
        if (password.isBlank()) return "Введите пароль"
        if (password.length < 6) return "Пароль минимум 6 символов"
        return null // всё ок
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        val validationError = validateFields(login, password)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(error = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.login(login, password)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    onSuccess()
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Ошибка входа"
                    )
                }
            }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)