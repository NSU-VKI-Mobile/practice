package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.data.model.GroupDto
import ci.nsu.mobile.auth.data.model.RegisterRequest
import ci.nsu.mobile.auth.data.model.UserDto
import ci.nsu.mobile.auth.data.remote.RetrofitClient
import ci.nsu.mobile.auth.data.remote.TokenManager
import ci.nsu.mobile.auth.data.remote.UserManager
import ci.nsu.mobile.auth.data.remote.api.AuthApi
import ci.nsu.mobile.domain.AuthManager
import ci.nsu.mobile.domain.AuthState
import ci.nsu.mobile.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthRepository : AuthManager {

    private val api: AuthApi = RetrofitClient.instance.create(AuthApi::class.java)
    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)

    suspend fun login(login: String, password: String): Result<String> {
        return try {
            val credentials = mapOf("login" to login, "password" to password)
            val response = api.login(credentials)
            TokenManager.token = response.token

            val users = api.getUsers()
            val currentUser = users.find { it.login == login }
            currentUser?.let { user ->
                user.id?.let { id ->
                    UserManager.userId = id.toLong()
                }
            }
            _authState.value = AuthState.LoggedIn
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            api.register(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            Result.success(api.getUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            Result.success(api.getGroups())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        val userId = UserManager.userId
        return if (userId != -1L) User(userId, "", "") else null
    }

    override fun isLoggedIn(): Boolean = UserManager.isLoggedIn()

    override fun logout() {
        TokenManager.clearToken()
        UserManager.clearUser()
        _authState.value = AuthState.LoggedOut
    }

    override fun observeAuthState(): Flow<AuthState> = _authState
}