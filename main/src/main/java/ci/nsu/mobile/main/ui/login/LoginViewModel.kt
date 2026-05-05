package ci.nsu.mobile.main.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = authRepository.login(login, password)
            _state.value = if (result.isSuccess) {
                _state.value.copy(isLoading = false, success = true)
            } else {
                _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка входа"
                )
            }
        }
    }

    fun resetSuccess() {
        _state.value = _state.value.copy(success = false)
    }
}

data class LoginState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)