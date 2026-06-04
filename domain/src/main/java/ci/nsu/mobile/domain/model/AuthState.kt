package ci.nsu.mobile.domain.model

sealed class AuthState {
    object Authenticated   : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}