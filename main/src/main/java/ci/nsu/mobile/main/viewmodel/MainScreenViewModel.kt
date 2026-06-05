package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.api.TokenManager
import ci.nsu.mobile.main.data.dto.UserDto
import ci.nsu.mobile.main.data.repositories.AuthRepository
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
            repository.getUsers()
                .onSuccess {
                    _users.value = it
                    println("USERS LOADED: ${it.size}")
                }
                .onFailure {
                    println("ERROR USERS: ${it.message}")
                    it.printStackTrace()
                }
        }
    }
}