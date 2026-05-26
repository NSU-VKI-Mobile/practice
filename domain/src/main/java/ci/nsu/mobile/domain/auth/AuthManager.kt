package ci.nsu.mobile.domain.auth

import ci.nsu.mobile.domain.common.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    suspend fun logout()
    fun observeAuthState(): Flow<AuthState>
    fun getCurrentUserId(): Long?
}