package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.UserDto  // <-- Добавьте
import ci.nsu.mobile.main.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<UserDto>) : UserListState()
    data class Error(val message: String) : UserListState()
}

class UserViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _usersState = MutableStateFlow<UserListState>(UserListState.Loading)
    val usersState: StateFlow<UserListState> = _usersState

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UserListState.Loading
            val result = repository.getUsers()
            _usersState.value = if (result.isSuccess) {
                UserListState.Success(result.getOrNull() ?: emptyList())
            } else {
                UserListState.Error(result.exceptionOrNull()?.message ?: "Failed to load users")
            }
        }
    }
}