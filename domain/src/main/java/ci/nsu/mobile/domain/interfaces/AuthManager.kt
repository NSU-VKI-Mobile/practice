package ci.nsu.mobile.domain.interfaces

import ci.nsu.mobile.domain.models.AuthState
import ci.nsu.mobile.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun getUsers(): List<User>
    suspend fun logout()
    fun observeAuthState(): Flow<AuthState>
}