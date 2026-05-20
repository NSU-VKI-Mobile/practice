package ci.nsu.mobile.main.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.token.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<UserDto>) : UsersState()
    data class Error(val message: String) : UsersState()
}

class UsersViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _usersState = MutableStateFlow<UsersState>(UsersState.Loading)
    val usersState: StateFlow<UsersState> = _usersState

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UsersState.Loading
            val result = repository.getUsers()
            _usersState.value = if (result.isSuccess)
                UsersState.Success(result.getOrDefault(emptyList()))
            else
                UsersState.Error(result.exceptionOrNull()?.message ?: "Ошибка загрузки")
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}
