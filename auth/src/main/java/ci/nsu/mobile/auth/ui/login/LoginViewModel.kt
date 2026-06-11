package ci.nsu.mobile.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.model.RegisterRequest
import ci.nsu.mobile.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        if (login.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Введите логин")
            return
        }
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Введите пароль")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.login(login, password)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка входа"
                )
            }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)