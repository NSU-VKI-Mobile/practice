package ci.nsu.mobile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.models.UserDto
import ci.nsu.mobile.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<UserDto>) : UsersState()
    data class Error(val message: String) : UsersState()
}

class UsersViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _usersState = MutableStateFlow<UsersState>(UsersState.Loading)
    val usersState: StateFlow<UsersState> = _usersState

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UsersState.Loading
            val result = repository.getUsers()
            _usersState.value = if (result.isSuccess) {
                UsersState.Success(result.getOrNull() ?: emptyList())
            } else {
                UsersState.Error(result.exceptionOrNull()?.message ?: "Failed to load users")
            }
        }
    }
}