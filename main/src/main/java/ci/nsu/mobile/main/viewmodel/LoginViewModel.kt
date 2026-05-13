package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.viewmodel.state.LoginEvents
import ci.nsu.mobile.main.viewmodel.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun loginEvent(event: LoginEvents) {
        when(event) {
            is LoginEvents.LoginChanged -> {
                _state.update { it.copy(login = event.newLogin) }
            }
            is LoginEvents.PasswordChanged -> {
                _state.update { it.copy(password = event.newPassword) }
            }
            is LoginEvents.SubmitLogin -> login()
            is LoginEvents.ValidationScreen -> validationLoginScreen()
            is LoginEvents.CleanAll ->  resetState()
        }
    }
    private fun validationLoginScreen(){
        val stateValue = _state.value
        if (stateValue.login.isEmpty()) {
            _state.update { it.copy(errorMessage = "login is empty") }
        }
        if (stateValue.password.isEmpty()) {
            _state.update { it.copy(errorMessage = "password is empty") }
        }
        else {
            _state.update { it.copy(isSuccess = true) }
        }
    }
    private fun login() {
        viewModelScope.launch {
            val result = repository.login(_state.value.login, _state.value.password)
            if (result.isSuccess) {
                _state.update { it.copy(isSuccess = true, errorMessage = null) }
            }
            if (result.isFailure) {
                _state.update { it.copy(isSuccess = false, errorMessage = "error login event")}
            }
        }
    }

    private fun resetState() {
        _state.update{it.copy(login = "")}
        _state.update{it.copy(password = "")}
        _state.update{it.copy(errorMessage = null)}
        _state.update{it.copy(isSuccess = false)}
    }
}