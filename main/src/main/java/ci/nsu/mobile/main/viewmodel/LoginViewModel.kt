package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.repository.AuthRepository
import ci.nsu.mobile.main.viewmodel.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(val repository: AuthRepository): ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun loginUpdate(newLogin: String) {
        _state.update { it.copy(login = newLogin) }
    }
    fun passwordUpdate(newPassword: String) {
        _state.update { it.copy(password = newPassword) }
    }
    fun validationLoginScreen(): Boolean {
        val stateValue = _state.value
        val result = !(stateValue.login.isEmpty() || stateValue.password.isEmpty())
        return result
    }
    fun login() {
        val stateValue = _state.value
        repository.login(stateValue.login, stateValue.password)
    }
}