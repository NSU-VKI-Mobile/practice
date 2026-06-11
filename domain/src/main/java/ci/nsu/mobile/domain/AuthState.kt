package ci.nsu.mobile.domain

sealed class AuthState {
    object LoggedIn : AuthState()
    object LoggedOut : AuthState()
    object Loading : AuthState()
}