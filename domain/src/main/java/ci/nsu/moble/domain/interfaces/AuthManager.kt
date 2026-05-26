package ci.nsu.moble.domain.interfaces

import ci.nsu.moble.domain.models.User
import ci.nsu.moble.domain.states.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi

interface AuthManager
{
    @OptIn(InternalSerializationApi::class)
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    suspend fun logout()
    fun observeAuthState(): Flow<AuthState>
}