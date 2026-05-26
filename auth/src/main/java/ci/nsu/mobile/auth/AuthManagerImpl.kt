package ci.nsu.mobile.auth

import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.domain.AuthManager
import ci.nsu.mobile.domain.AuthState
import ci.nsu.mobile.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthManagerImpl(private val repository: AuthRepository) : AuthManager {
    
    private val _authState = MutableStateFlow<AuthState>(if (isLoggedIn()) AuthState.Authenticated(getCurrentUser()!!) else AuthState.Unauthenticated)

    override fun getCurrentUser(): User? {
        val id = TokenManager.userId ?: return null
        return User(id, "User", "") // We might need to store/fetch more details
    }

    override fun isLoggedIn(): Boolean = repository.isAuthenticated()

    override fun logout() {
        repository.logout()
        _authState.value = AuthState.Unauthenticated
    }

    override fun observeAuthState(): Flow<AuthState> = _authState.asStateFlow()
}