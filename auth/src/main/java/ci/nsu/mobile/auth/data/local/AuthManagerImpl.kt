package ci.nsu.mobile.auth.data.local

import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.auth.AuthState
import ci.nsu.mobile.domain.model.User
import kotlinx.coroutines.flow.Flow

class AuthManagerImpl : AuthManager {
    override fun getCurrentUser(): User? = TokenManager.currentUser()

    override fun isLoggedIn(): Boolean = TokenManager.isLoggedIn()

    override fun logout() {
        TokenManager.clear()
    }

    override fun observeAuthState(): Flow<AuthState> = TokenManager.authState
}
