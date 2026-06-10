package ci.nsu.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.ui.common.NetworkResult // Точный импорт по вашей структуре папок
import ci.nsu.data.remote.dto.AuthResponse
import ci.nsu.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Состояние авторизации (изначально null)
    private val _authState = MutableStateFlow<NetworkResult<AuthResponse>?>(null)
    val authState: StateFlow<NetworkResult<AuthResponse>?> = _authState

    // Метод для входа в систему
    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = NetworkResult.Error("Поля не могут быть пустыми")
            return
        }

        viewModelScope.launch {
            _authState.value = NetworkResult.Loading
            try {
                val response = authRepository.login(username, password)
                if (response.isSuccessful && response.body() != null) {
                    _authState.value = NetworkResult.Success(response.body()!!)
                } else {
                    _authState.value = NetworkResult.Error("Ошибка авторизации: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = NetworkResult.Error("Ошибка сети: ${e.localizedMessage}")
            }
        }
    }

    // Метод для регистрации нового пользователя
    fun register(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = NetworkResult.Error("Заполните все поля")
            return
        }

        viewModelScope.launch {
            _authState.value = NetworkResult.Loading
            try {
                val response = authRepository.register(username, email, password)
                if (response.isSuccessful && response.body() != null) {
                    _authState.value = NetworkResult.Success(response.body()!!)
                } else {
                    _authState.value = NetworkResult.Error("Ошибка регистрации: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = NetworkResult.Error("Ошибка сети: ${e.localizedMessage}")
            }
        }
    }

    // Сброс текущего состояния
    fun resetState() {
        _authState.value = null
    }

    // Выход из аккаунта
    fun logout() {
        authRepository.logout()
        resetState()
    }
}
