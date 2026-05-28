package ci.nsu.mobile.auth.di

import ci.nsu.mobile.auth.utils.UserPreferences
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.models.AuthState
import ci.nsu.mobile.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthManagerImpl(
    private val userPreferences: UserPreferences
) : AuthManager {

    override suspend fun isLoggedIn(): Boolean {
        return userPreferences.getToken() != null
    }

    override suspend fun getCurrentUser(): User? {
        val userId = userPreferences.getUserId() ?: return null
        // По идее тут должен быть запрос к API, но наш сервер не возвращает нам профиль так что...
        return User(
            id = userId,
            login = "",
            email = ""
        )
    }

    override suspend fun logout() {
        userPreferences.clear()
    }

    override fun observeAuthState(): Flow<AuthState> {
        return userPreferences.getTokenFlow().map { token ->
            if (token != null) AuthState.Authenticated else AuthState.Unauthenticated
        }
    }
}