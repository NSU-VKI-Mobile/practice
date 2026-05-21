package ci.nsu.mobile.main.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            val result = repository.login(login, password)
            result.fold(
                onSuccess = {
                    _uiState.value = LoginUiState(isSuccess = true)
                },
                onFailure = {
                    _uiState.value = LoginUiState(error = it.message)
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}