package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.repositories.AuthRepository
import ci.nsu.moble.main.viewmodel.states.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager,
    private val savedState: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LoginUiState(
            login = savedState["login"] ?: "",
            pass = savedState["pass"] ?: ""
        )
    )
    val uiState = _uiState.asStateFlow()

    fun onLoginChanged(newValue: String) {
        savedState["login"] = newValue
        _uiState.update { it.copy(login = newValue) }
    }

    fun onPasswordChanged(newValue: String) {
        savedState["pass"] = newValue
        _uiState.update { it.copy(pass = newValue) }
    }

    fun login(onSuccess: () -> Unit) {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.login(current.login, current.pass)
                .onSuccess { loginResponse ->
                    // Поправлено: используем метод saveToken вместо прямого сеттера
                    tokenManager.saveToken(loginResponse.token)
                }
                .fold(
                    onSuccess = {
                        repository.getUserByLogin(current.login)
                    },
                    onFailure = { error ->
                        Result.failure(error)
                    }
                )
                .onSuccess { userDto ->
                    // Поправлено: используем метод saveCurrentUser
                    tokenManager.saveCurrentUser(userDto)
                    resetState()
                    onSuccess()
                }
                .onFailure { e ->
                    // Если произошла ошибка на любом из этапов, полностью зачищаем сессию
                    tokenManager.clear()
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }

    private fun resetState() {
        // Сброс сохраненного состояния
        savedState["login"] = ""
        savedState["pass"] = ""
        savedState["isLoading"] = false
        savedState["error"] = null
        // Сброс UI состояния
        _uiState.update { it.copy(login = "", pass = "", isLoading = false, error = null) }
    }
}