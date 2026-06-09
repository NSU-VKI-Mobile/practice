package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun updateLogin(value: String) {
        _login.update { value }
    }

    fun updatePassword(value: String) {
        _password.update { value }
    }

    fun clearError() {
        _error.update { null }
    }

    fun onLoginClick(onSuccess: () -> Unit) {
        if (_login.value.isBlank() || _password.value.isBlank()) {
            _error.update { "Заполните все поля" }
            return
        }

        viewModelScope.launch {
            _isLoading.update { true }
            _error.update { null }

            val result = authRepository.login(_login.value, _password.value)

            _isLoading.update { false }

            result.onSuccess {
                onSuccess()
            }.onFailure { exception ->
                _error.update { exception.message ?: "Ошибка входа" }
            }
        }
    }
}