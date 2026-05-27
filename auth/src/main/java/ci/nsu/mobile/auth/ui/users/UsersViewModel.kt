package ci.nsu.mobile.auth.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.repository.UserRepository
import ci.nsu.mobile.auth.data.model.Result
import ci.nsu.mobile.domain.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            val currentUser = authManager.getCurrentUser()

            _uiState.update {
                it.copy(
                    usersState = UsersState.Loading,
                    currentUserId = currentUser?.id
                )
            }

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

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            authManager.logout()
            onComplete()
        }
    }

    fun getCurrentUserId(): Long? = authManager.getCurrentUserId()
}