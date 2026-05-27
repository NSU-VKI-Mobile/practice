package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val errorMessage: String? = null
)

class MainViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = MainUiState(isLoading = true)

            val result = repository.getUsers()

            _uiState.value = if (result.isSuccess) {
                MainUiState(
                    isLoading = false,
                    users = result.getOrNull() ?: emptyList()
                )
            } else {
                MainUiState(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to load users"
                )
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}