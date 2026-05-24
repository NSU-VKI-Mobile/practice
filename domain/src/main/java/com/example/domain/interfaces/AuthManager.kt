package com.example.domain.interfaces

import com.example.domain.model.AuthState
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): Flow<AuthState>
}