package ci.nsu.mobile.auth.viewModels.userOwn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.domain.interfaces.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserOwnViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {
    private val _state = MutableStateFlow(UserOwnState())
    val state: StateFlow<UserOwnState> = _state.asStateFlow()

    fun loadCurrentUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentUser = authManager.getCurrentUser()

            if (currentUser != null) {
                _state.update {
                    it.copy(
                        userLogin = currentUser.login,
                        userId = currentUser.userId,
                        userEmail = currentUser.email,
                        userPhone = currentUser.phoneNumber,
                        userCreatedDate = currentUser.createdDate,
                        userPersonId = currentUser.personId,
                        userLastLoginDate = currentUser.lastLoginDate,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Пользователь не авторизован"
                    )
                }
            }
        }
    }
}