package com.example.auth

import com.example.auth.data.network.TokenManager
import com.example.auth.data.repository.AuthRepository
import com.example.domain.interfaces.AuthManager
import com.example.domain.models.AuthState
import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Singleton
class AuthManagerImpl(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) : AuthManager {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)

    override fun getCurrentUser(): User? {
        return if (tokenManager.isLoggedIn()) {
            User(
                userId = tokenManager.userId,
                login = tokenManager.userLogin ?: "",
                email = "",
                phoneNumber = null,
                roleId = 1,
                authAllowed = true,
                personId = -1,
                createdDate = "",
                lastLoginDate = null
            )
        } else null
    }

    override fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()

    override fun logout() {
        tokenManager.clear()
        _authState.value = AuthState.Unauthenticated
    }

    override fun observeAuthState(): Flow<AuthState> = _authState

    override suspend fun getUsers(): List<User> {
        val result = authRepository.getUsers()
        return result.getOrElse { emptyList() }
    }
}