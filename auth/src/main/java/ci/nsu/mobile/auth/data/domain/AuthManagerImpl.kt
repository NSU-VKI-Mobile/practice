package ci.nsu.mobile.auth.data.domain

import ci.nsu.mobile.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.auth.AuthState
import ci.nsu.mobile.domain.common.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManagerImpl @Inject constructor(
    private val tokenManager: TokenManager
) : AuthManager {

    private val _authState = MutableStateFlow<AuthState>(
        if (tokenManager.token != null && tokenManager.userId != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    )

    override fun getCurrentUserId(): Long? = tokenManager.userId

    override fun getCurrentUser(): User? {
        val userId = tokenManager.userId ?: return null
        val login = tokenManager.getUserLoginFromToken() ?: return null
        return User(id = userId, login = login)
    }

    override fun isLoggedIn(): Boolean = tokenManager.userId != null

    override suspend fun logout() {
        tokenManager.clear()
        _authState.value = AuthState.Unauthenticated
    }

    override fun observeAuthState(): Flow<AuthState> = _authState.asStateFlow()
}