package ci.nsu.mobile.auth.data

import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.auth.AuthState
import ci.nsu.mobile.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class AuthManagerImpl(private val apiService: ApiService) : AuthManager {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)

    override fun isLoggedIn(): Boolean = TokenManager.isLoggedIn()

    override fun getCurrentUserLogin(): String? = TokenManager.login

    override fun logout() {
        TokenManager.logout()
        _authState.value = AuthState.Idle
    }

    override fun observeAuthState(): Flow<AuthState> = _authState

    override suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.login(LoginRequest(login, password))
                TokenManager.token = response.token

                val user = apiService.getUserByLogin(login)
                TokenManager.userId = user.userId
                TokenManager.login = login // Сохраняем логин
            }
            _authState.value = AuthState.Success
            Result.success(Unit)
        } catch (e: Exception) {
            TokenManager.logout()
            _authState.value = AuthState.Error(e.message ?: "Ошибка входа")
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                apiService.register(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            Result.success(apiService.getGroups())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            Result.success(apiService.getUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}