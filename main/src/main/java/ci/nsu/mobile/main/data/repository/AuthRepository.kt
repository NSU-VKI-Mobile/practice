package ci.nsu.mobile.main.data.repository

import android.util.Log
import ci.nsu.mobile.main.data.api.ApiService
import ci.nsu.mobile.main.data.models.LoginRequest
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.TokenResponse
import ci.nsu.mobile.main.data.token.TokenManager
import java.io.IOException

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun login(login: String, password: String): Result<Pair<TokenResponse, UserDto>> {
        return try {
            val tokenResponse = apiService.login(LoginRequest(login, password))
            Log.d("AuthRepo", "Login success, token: ${tokenResponse.token}")
            tokenManager.saveToken(tokenResponse.token)

            // Получаем пользователя по логину из токена
            val userLogin = tokenManager.getUserLogin()
            Log.d("AuthRepo", "Getting user by login: $userLogin")
            val currentUser = apiService.getUserByLogin(userLogin)
            Log.d("AuthRepo", "Got user: id=${currentUser.id}, login=${currentUser.login}")
            tokenManager.saveUserId(currentUser.id.toLong())

            Result.success(Pair(tokenResponse, currentUser))
        } catch (e: IOException) {
            Log.e("AuthRepo", "Network error: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("AuthRepo", "Login failed: ${e.message}")
            Result.failure(Exception("Login failed: ${e.message}"))
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = apiService.register(request)
            Log.d("AuthRepo", "Register success")
            Result.success(response)
        } catch (e: IOException) {
            Log.e("AuthRepo", "Network error: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("AuthRepo", "Registration failed: ${e.message}")
            Result.failure(Exception("Registration failed: ${e.message}"))
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Log.d("AuthRepo", "Get users success, count: ${users.size}")
            Result.success(users)
        } catch (e: IOException) {
            Log.e("AuthRepo", "Network error: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("AuthRepo", "Failed to get users: ${e.message}")
            Result.failure(Exception("Failed to get users: ${e.message}"))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Log.d("AuthRepo", "Get groups success, count: ${groups.size}")
            Result.success(groups)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get groups: ${e.message}"))
        }
    }

    fun logout() {
        Log.d("AuthRepo", "Logout, clearing token")
        tokenManager.clearToken()
    }

    fun isLoggedIn(): Boolean {
        return tokenManager.hasToken()
    }
}