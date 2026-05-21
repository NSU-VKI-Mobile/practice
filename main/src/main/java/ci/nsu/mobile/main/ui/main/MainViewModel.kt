package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.token.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val error: String? = null
)

class MainViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = MainUiState(isLoading = true)

            val result = repository.getUsers()
            result.fold(
                onSuccess = { users ->
                    _uiState.value = MainUiState(users = users)
                },
                onFailure = {
                    _uiState.value = MainUiState(error = it.message)
                }
            )
        }
    }

    fun logout() {
        TokenManager.clearToken()
    }
}