package ci.nsu.mobile.domain.auth

import ci.nsu.mobile.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Public authentication API exposed by the auth module.
 *
 * App-level code should depend on this contract instead of concrete token
 * storage, repositories, or screens from the auth feature.
 */
interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): Flow<AuthState>
}
