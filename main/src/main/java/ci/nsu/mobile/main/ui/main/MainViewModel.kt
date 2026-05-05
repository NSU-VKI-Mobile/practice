package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.domain.AuthRepository
import ci.nsu.mobile.main.data.models.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = authRepository.getUsers()
            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    isLoading = false,
                    users = result.getOrNull() ?: emptyList()
                )
            } else {
                _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Ошибка загрузки"
                )
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _state.value = _state.value.copy(loggedOut = true)
    }
}

data class MainState(
    val isLoading: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val error: String? = null,
    val loggedOut: Boolean = false
)