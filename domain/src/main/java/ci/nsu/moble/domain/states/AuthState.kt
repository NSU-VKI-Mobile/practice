package ci.nsu.moble.domain.states

import ci.nsu.moble.domain.models.User
import kotlinx.serialization.InternalSerializationApi

sealed class AuthState
{
    object Unauthenticated : AuthState()
    data class Authenticated @OptIn(InternalSerializationApi::class) constructor(val user: User) : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}