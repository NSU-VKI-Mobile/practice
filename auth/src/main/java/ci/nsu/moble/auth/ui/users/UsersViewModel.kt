package ci.nsu.moble.auth.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.auth.data.repository.AuthRepository
import ci.nsu.moble.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

class UsersViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    sealed class UsersState {
        object FirstLaunch : UsersState()
        object Loading : UsersState()
        data class Success @OptIn(InternalSerializationApi::class) constructor(val users: List<User>) : UsersState()
        data class Error(val message: String) : UsersState()
    }

    private val _state = MutableStateFlow<UsersState>(UsersState.FirstLaunch)
    val state: StateFlow<UsersState> = _state

    @OptIn(InternalSerializationApi::class)
    fun loadUsers() {
        if (_state.value is UsersState.Loading || _state.value is UsersState.Success) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = UsersState.Loading
            try {
                val result = repository.getUsers()
                _state.value = if (result.isSuccess) {
                    UsersState.Success(result.getOrNull() ?: emptyList())
                } else {
                    UsersState.Error(result.exceptionOrNull()?.message ?: "Ошибка загрузки")
                }
            } catch (e: Exception) {
                _state.value = UsersState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}