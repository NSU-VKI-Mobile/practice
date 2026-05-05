package ci.nsu.mobile.main.features.auth.register

import android.util.Log
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.core.base.AppEvent
import ci.nsu.mobile.main.core.base.BaseViewModel
import ci.nsu.mobile.main.core.navigation.Screen
import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.GroupRepository
import ci.nsu.mobile.main.model.dto.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val groupRepository: GroupRepository,
): BaseViewModel<AppEvent, RegisterState>(
    initialState = RegisterState()
) {
    init {
        initDefaults()
    }

    private fun initDefaults() {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(
                    groups = groupRepository.getGroups().getOrNull()!!
                )
            }

        }
    }

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

    fun registerRequest(request: RegisterRequest) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.register(request)
                .onSuccess { _event.emit(AppEvent.Navigate(Screen.Main.route)) }
                .onFailure { e -> Log.d("1234", e.toString())}
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun register() {
        validateAll()
        val currentState = _state.value
        if (!currentState.isButtonEnabled) return
        _state.update { it.copy(isButtonEnabled = false) }
        registerRequest(currentState.toRegisterRequest())
        _state.update { it.copy(isButtonEnabled = true) }
    }

    fun toLogin() {
        viewModelScope.launch {
            _event.emit(AppEvent.Navigate(Screen.Login.route))
        }
    }
}