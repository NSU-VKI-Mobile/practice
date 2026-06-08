package ci.nsu.mobile.auth.navigation

import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.AuthState
import ci.nsu.mobile.domain.models.User
import ci.nsu.mobile.domain.token.ITokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManagerImpl @Inject constructor(
    private val tokenManager: ITokenManager
) : AuthManager {

    override fun getCurrentUser(): User? {
        return if (isLoggedIn()) {
            val userId = tokenManager.userId ?: return null
            val login = tokenManager.userLogin ?: return null
            User(
                userId = userId,
                login = login,
                email = tokenManager.userEmail ?: "",
                personId = tokenManager.userPersonId ?: 0,
                createdDate = tokenManager.userCreatedDate ?: "",
                phoneNumber = tokenManager.userPhone,
                roleId = tokenManager.userRoleId ?: 1,
                authAllowed = true,
                lastLoginDate = tokenManager.userLastLoginDate
            )
        } else null
    }

    override fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }

    override fun logout() {
        kotlinx.coroutines.runBlocking {
            tokenManager.clear()
        }
    }

    override fun observeAuthState(): Flow<AuthState> {
        return tokenManager.observeToken().map { token ->
            if (token != null) AuthState.Authenticated
            else AuthState.Unauthenticated
        }
    }
}