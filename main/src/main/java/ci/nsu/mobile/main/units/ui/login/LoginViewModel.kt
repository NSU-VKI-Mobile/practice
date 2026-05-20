package ci.nsu.mobile.main.units.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.units.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())

    fun updateLogin(login: String){
        _uiState.value = _uiState.value.copy(login=login)
    }
    fun updatePassword(password: String){
        _uiState.value = _uiState.value.copy(password=password)
    }
    fun login(onSuccess: (String) -> Unit){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.login(_uiState.value.login, _uiState.value.password)
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess(user.login)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }
    data class LoginUiState(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )
}