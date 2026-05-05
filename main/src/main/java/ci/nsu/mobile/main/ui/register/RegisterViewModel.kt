package ci.nsu.mobile.main.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.domain.AuthRepository
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingGroups = true)
            val result = authRepository.getGroups()
            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    isLoadingGroups = false,
                    groups = result.getOrNull() ?: emptyList(),
                    groupsError = null
                )
            } else {
                _state.value.copy(
                    isLoadingGroups = false,
                    groupsError = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = authRepository.register(request)
            _state.value = if (result.isSuccess) {
                _state.value.copy(isLoading = false, success = true)
            } else {
                _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка регистрации"
                )
            }
        }
    }

    fun resetSuccess() {
        _state.value = _state.value.copy(success = false)
    }
}

data class RegisterState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val isLoadingGroups: Boolean = false,
    val groups: List<GroupDto> = emptyList(),
    val groupsError: String? = null
)