package ci.nsu.moble.auth.manager

import ci.nsu.moble.auth.data.repository.AuthRepository
import ci.nsu.moble.auth.data.storage.TokenManager
import ci.nsu.moble.domain.interfaces.AuthManager
import ci.nsu.moble.domain.models.User
import ci.nsu.moble.domain.states.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

class AuthManagerImpl(
    private val tokenManager: TokenManager,
    private val repository: AuthRepository
) : AuthManager {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        checkAuthStatus()
    }

    @OptIn(InternalSerializationApi::class)
    private fun checkAuthStatus() {
        if (tokenManager.isLoggedIn()) {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                scope.launch {
                    val user = getUserFromServer(userId)
                    _authState.value = user?.let { AuthState.Authenticated(it) } ?: AuthState.Unauthenticated
                }
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    private suspend fun getUserFromServer(userId: Long): User? {
        val users = repository.getUsers().getOrNull()
        return users?.find { it.userId == userId }?.let { userDto ->
            User(
                userId = userDto.userId,
                login = userDto.login,
                email = userDto.email,
                phoneNumber = userDto.phoneNumber,
                roleId = userDto.roleId,
                authAllowed = userDto.authAllowed
            )
        }
    }

    @OptIn(InternalSerializationApi::class)
    override fun getCurrentUser(): User? {
        return (_authState.value as? AuthState.Authenticated)?.user
    }

    override fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }

    override suspend fun logout() {
        tokenManager.clearAll()
        _authState.value = AuthState.Unauthenticated
    }

    override fun observeAuthState(): Flow<AuthState> = _authState.asStateFlow()
}