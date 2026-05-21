package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
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

sealed class GroupListState {
    object Loading : GroupListState()
    data class Success(val groups: List<ci.nsu.mobile.main.data.models.GroupDto>) : GroupListState()
    data class Error(val message: String) : GroupListState()
}

class GroupViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _groupsState = MutableStateFlow<GroupListState>(GroupListState.Loading)
    val groupsState: StateFlow<GroupListState> = _groupsState

    fun loadGroups() {
        viewModelScope.launch {
            _groupsState.value = GroupListState.Loading
            val result = repository.getGroups()
            _groupsState.value = if (result.isSuccess) {
                GroupListState.Success(result.getOrNull() ?: emptyList())
            } else {
                GroupListState.Error(result.exceptionOrNull()?.message ?: "Failed to load groups")
            }
        }
    }
}