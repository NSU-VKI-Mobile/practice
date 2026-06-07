package ci.nsu.mobile.auth.viewModels.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        loadUsers()
    }

    fun userEvent(event: UserEvents) {
        when(event) {
            is UserEvents.BottomItemChanged -> {
                _state.update { it.copy(selectedBottomItem = event.newItem) }
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = repository.getUsers()
            result.onSuccess { users ->
                _state.update { it.copy(users = users, isLoading = false, errorMessage = null) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, errorMessage = "Ошибка загрузки пользователей: ${error.message}") }
            }
        }
    }
}