package ci.nsu.mobile.main.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.datasource.local.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.model.Result
import ci.nsu.mobile.main.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(usersState = UsersState.Loading,
                currentUserId = tokenManager.userId ) }

            val result = userRepository.getUsers()

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(usersState = UsersState.Success(result.data))
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(usersState = UsersState.Error(result.message))
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun getCurrentUserId(): Long? = tokenManager.userId
}