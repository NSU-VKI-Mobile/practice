package ci.nsu.mobile.main.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
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
