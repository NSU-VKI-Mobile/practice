package ci.nsu.mobile.domain.interfaces

import ci.nsu.mobile.domain.model.AuthState
import ci.nsu.mobile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): Flow<AuthState>
}