package ci.nsu.mobile.domain.auth

import ci.nsu.mobile.domain.model.User

/**
 * Authentication state shared between modules.
 */
sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
}
