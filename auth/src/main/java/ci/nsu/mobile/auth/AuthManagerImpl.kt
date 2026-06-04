package ci.nsu.mobile.auth

import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.model.AuthState
import ci.nsu.mobile.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthManagerImpl : AuthManager {

    override fun getCurrentUser(): User? {
        val id = TokenManager.userId ?: return null
        return User(id = id, login = "")   // login неизвестен из токена
    }

    override fun isLoggedIn(): Boolean = TokenManager.token != null

    override fun logout() = TokenManager.clear()

    override fun observeAuthState(): Flow<AuthState> = flow {
        emit(
            if (TokenManager.token != null) AuthState.Authenticated
            else AuthState.Unauthenticated
        )
    }
}