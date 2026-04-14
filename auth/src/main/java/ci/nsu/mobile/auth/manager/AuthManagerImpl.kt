package ci.nsu.mobile.auth.manager

import ci.nsu.mobile.auth.data.TokenManager
import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.models.User
import ci.nsu.mobile.domain.state.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthManagerImpl : AuthManager {

    private val _authState = MutableStateFlow<AuthState>(
        if (isLoggedIn()) AuthState.Authenticated(getDummyUser())
        else AuthState.Unauthenticated
    )

    override fun getCurrentUser(): User? {
        if (!isLoggedIn()) return null
        return getDummyUser()
    }

    override fun isLoggedIn(): Boolean {
        return TokenManager.token != null && TokenManager.userId != -1
    }

    override fun logout() {
        TokenManager.clear()
        _authState.value = AuthState.Unauthenticated
    }

    override fun observeAuthState(): Flow<AuthState> = _authState

    private fun getDummyUser(): User {
        return User(
            userId = TokenManager.userId,
            login = "Unknown",
            email = "Unknown",
            roleId = 1,
            authAllowed = true,
            personId = -1
        )
    }
}