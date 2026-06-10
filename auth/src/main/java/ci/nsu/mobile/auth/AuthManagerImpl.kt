package ci.nsu.mobile.auth

import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.domain.AuthManager
import ci.nsu.mobile.domain.AuthState
import ci.nsu.mobile.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthManagerImpl(
    private val repository: AuthRepository
) : AuthManager {

    private val _authState = MutableStateFlow<AuthState>(
        if (repository.isAuthenticated()) {
            val user = getCurrentUser()
            if (user != null) AuthState.Authenticated(user)
            else AuthState.Unauthenticated
        } else AuthState.Unauthenticated
    )

    override fun getCurrentUser(): User? {
        val userId = TokenManager.userId ?: return null
        // В реальном приложении здесь можно загрузить пользователя из репозитория
        return User(userId, "User", "")
    }

    override fun isLoggedIn(): Boolean = repository.isAuthenticated()

    override fun logout() {
        repository.logout()
        _authState.value = AuthState.Unauthenticated
    }

    override fun observeAuthState(): Flow<AuthState> = _authState.asStateFlow()

    // Вспомогательный метод для обновления состояния после логина
    fun updateAuthState(user: User) {
        _authState.value = AuthState.Authenticated(user)
    }
}