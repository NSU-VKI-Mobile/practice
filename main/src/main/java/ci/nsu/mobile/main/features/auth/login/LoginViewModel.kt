package ci.nsu.mobile.main.features.auth.login

import javax.inject.Inject

import android.util.Log

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.core.base.AppEvent
import ci.nsu.mobile.main.core.base.BaseViewModel

import ci.nsu.mobile.main.core.navigation.Screen
import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.model.dto.LoginRequest

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
): BaseViewModel<AppEvent, LoginState>(
    initialState = LoginState()
) {
    fun validateAll() {
        _state.update { current ->
            val newErrors = current.fields.mapValues { (field, value) ->
                validateField(field, value)
            }
            current.copy(
                fieldErrors = newErrors,
                isButtonEnabled = newErrors.values.all { it.isEmpty() }
            )
        }
    }
    fun onFieldChange(field: Field, value: FieldValue) {
        _state.update { current ->
            val newErrors = current.fieldErrors + (field to validateField(field, value))

            current.copy(
                fields = current.fields + (field to value),
                fieldErrors = newErrors,
                isButtonEnabled = newErrors.values.all { it.isEmpty() }
            )
        }
    }

    fun loginRequest(request: LoginRequest) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.login(request)
                .onSuccess { _event.emit(AppEvent.Navigate(Screen.Main.route)) }
                .onFailure { e -> Log.d("1234", e.toString())}
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun login() {
        validateAll()
        val currentState = _state.value
        if (!currentState.isButtonEnabled) return
        _state.update { it.copy(isButtonEnabled = false) }
        loginRequest(currentState.toLoginRequest())
        _state.update { it.copy(isButtonEnabled = true) }
    }

    fun toRegister() {
        viewModelScope.launch {
            _event.emit(AppEvent.Navigate(Screen.Register.route))
        }
    }
}