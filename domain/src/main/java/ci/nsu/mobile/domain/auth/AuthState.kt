package ci.nsu.mobile.domain.auth

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Loading(val isLoading: Boolean) : AuthState()
}