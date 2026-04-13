package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.util.Event
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    data class UiState(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val repository = AuthRepository()

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _navigateToMain = MutableLiveData<Event<Unit>>()
    val navigateToMain: LiveData<Event<Unit>> = _navigateToMain

    private val _navigateToRegister = MutableLiveData<Event<Unit>>()
    val navigateToRegister: LiveData<Event<Unit>> = _navigateToRegister

    fun onLoginChanged(value: String) {
        _uiState.value = (_uiState.value ?: UiState()).copy(login = value, error = null)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = (_uiState.value ?: UiState()).copy(password = value, error = null)
    }

    fun onRegisterClicked() {
        _navigateToRegister.value = Event(Unit)
    }

    fun onLoginClicked() {
        val current = _uiState.value ?: UiState()
        if (current.login.isBlank() || current.password.isBlank()) {
            _uiState.value = current.copy(error = "Введите логин и пароль")
            return
        }

        _uiState.value = current.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = repository.login(current.login.trim(), current.password)
            result.fold(
                onSuccess = {
                    _uiState.value = (_uiState.value ?: UiState()).copy(isLoading = false)
                    _navigateToMain.value = Event(Unit)
                },
                onFailure = { throwable ->
                    _uiState.value = (_uiState.value ?: UiState()).copy(
                        isLoading = false,
                        error = throwable.message ?: "Ошибка входа"
                    )
                }
            )
        }
    }
}
