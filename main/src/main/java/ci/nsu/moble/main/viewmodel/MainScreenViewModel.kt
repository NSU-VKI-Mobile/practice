package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.dto.UserDto
import ci.nsu.moble.main.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainScreenViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users

    fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers().onSuccess {
                _users.value = it
            }
        }
    }
}