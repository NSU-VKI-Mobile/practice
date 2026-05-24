package com.example.auth.manager

import com.example.auth.api.TokenManager
import com.example.domain.interfaces.AuthManager
import com.example.domain.model.AuthState
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthManagerImpl : AuthManager {

    override fun getCurrentUser(): User? {
        val id = TokenManager.userId ?: return null
        return User(id = id, login = "")
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