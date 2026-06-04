package com.example.integratedapp.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integratedapp.data.model.UserDto
import com.example.integratedapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UsersUiState(
    val users: List<UserDto> = emptyList(),
    val selectedUser: UserDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class UsersViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getUsers().onSuccess { users ->
                _uiState.update { it.copy(isLoading = false, users = users) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun selectUser(user: UserDto?) {
        _uiState.update { it.copy(selectedUser = user) }
    }
}
