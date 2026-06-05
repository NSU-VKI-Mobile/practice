package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.api.TokenManager
import ci.nsu.mobile.main.data.repositories.AuthRepository
import ci.nsu.mobile.main.viewmodel.states.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(login: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)

            repository.login(login, pass)
                .onSuccess { response  ->
                    // Сохраняем токен
                    tokenManager.token = response.token
                    _state.value = AuthState(isLoggedIn = true)
                    onSuccess()
                }
                .onFailure { error ->
                    _state.value = AuthState(error = error.message ?: "Ошибка входа")
                }
        }
    }


    fun logout() {
        tokenManager.clear()
        _state.value = AuthState(isLoggedIn = false)
    }
}