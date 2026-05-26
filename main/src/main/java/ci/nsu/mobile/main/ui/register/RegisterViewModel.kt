package ci.nsu.mobile.main.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log
data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registrationSuccess: Boolean = false,
    val groups: List<GroupDto> = emptyList()
)

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    init {
        fetchGroups()
    }

    private fun fetchGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.getGroups()
                .onSuccess { groups ->
                    Log.d("RegisterViewModel", "Groups loaded: ${groups.size}")
                    _uiState.update { it.copy(isLoading = false, groups = groups) }
                }
                .onFailure { throwable ->
                    Log.e("RegisterViewModel", "Error loading groups", throwable)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Ошибка загрузки групп"
                        )
                    }
                }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            authRepository.register(request)
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false, registrationSuccess = true)
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Ошибка регистрации"
                        )
                    }
                }
        }
    }
}