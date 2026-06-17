package ci.nsu.mobile.domain.auth

import ci.nsu.mobile.domain.model.GroupDto
import ci.nsu.mobile.domain.model.RegisterRequest
import ci.nsu.mobile.domain.model.UserDto
import kotlinx.coroutines.flow.Flow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

interface AuthManager {
    fun isLoggedIn(): Boolean
    fun getCurrentUserLogin(): String?
    suspend fun login(login: String, password: String): Result<Unit>
    suspend fun register(request: RegisterRequest): Result<Unit>
    fun logout()
    fun observeAuthState(): Flow<AuthState>
    suspend fun getGroups(): Result<List<GroupDto>>
    suspend fun getUsers(): Result<List<UserDto>>
}