package ci.nsu.mobile.domain.state

import ci.nsu.mobile.domain.models.User

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}