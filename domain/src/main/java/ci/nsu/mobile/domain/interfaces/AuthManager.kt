package ci.nsu.mobile.domain.interfaces

import ci.nsu.mobile.domain.models.AuthState
import ci.nsu.mobile.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun logout()
    fun observeAuthState(): Flow<AuthState>
}