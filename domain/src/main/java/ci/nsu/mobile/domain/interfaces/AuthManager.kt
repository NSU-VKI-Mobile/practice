package ci.nsu.mobile.domain.interfaces

import ci.nsu.mobile.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): Flow<AuthState>
}
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}