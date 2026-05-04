package ci.nsu.mobile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.local.TokenManager
import ci.nsu.mobile.auth.data.model.GroupDto
import ci.nsu.mobile.auth.data.model.RegisterRequest
import ci.nsu.mobile.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val groups: List<GroupDto> = emptyList()
)

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun fetchGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groups -> _uiState.update { it.copy(groups = groups) } }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(error = error.message ?: "Не удалось загрузить группы")
                    }
                }
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            repository.register(request)
                .onSuccess {
                    TokenManager.login = request.login
                    TokenManager.password = request.password
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Ошибка регистрации")
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
