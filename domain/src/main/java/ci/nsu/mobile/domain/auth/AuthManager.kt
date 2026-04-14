package ci.nsu.mobile.domain.auth

import ci.nsu.mobile.domain.models.User
import ci.nsu.mobile.domain.state.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): Flow<AuthState>
}