package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivityViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val KEY_LOGIN = "login"
    private val KEY_PASSWORD = "password"

    private val _login = MutableStateFlow(savedStateHandle.get<String>(KEY_LOGIN) ?: "")
    val login: StateFlow<String> = _login.asStateFlow()

    private val _password = MutableStateFlow(savedStateHandle.get<String>(KEY_PASSWORD) ?: "")
    val password: StateFlow<String> = _password.asStateFlow()

    fun updateLogin(newValue: String) {
        _login.update { newValue }
        savedStateHandle[KEY_LOGIN] = newValue
    }

    fun updatePassword(newValue: String) {
        _password.update { newValue }
        savedStateHandle[KEY_PASSWORD] = newValue
    }
}