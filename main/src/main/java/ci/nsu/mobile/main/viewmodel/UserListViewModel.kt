package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.UserDto
import ci.nsu.mobile.main.repository.AuthRepository
import ci.nsu.mobile.main.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _usersState = MutableStateFlow(UserListUiState())
    val usersState: StateFlow<UserListUiState> = _usersState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UserListUiState(isLoading = true)

            val result = repository.getUsers()

            when (result) {
                is Result.Success -> {
                    _usersState.value = UserListUiState(
                        users = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _usersState.value = UserListUiState(
                        error = result.message,
                        isLoading = false
                    )
                }
                else -> {}
            }
        }
    }

    fun logout() {
        repository.logout()
    }

    data class UserListUiState(
        val users: List<UserDto> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}