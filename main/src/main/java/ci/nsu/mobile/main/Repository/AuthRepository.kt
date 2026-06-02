package ci.nsu.mobile.main.Repository

import ci.nsu.mobile.main.Auth.TokenManager
import ci.nsu.mobile.main.Data.Models.GroupDto
import ci.nsu.mobile.main.Data.Models.LoginRequest
import ci.nsu.mobile.main.Data.Models.RegisterRequest
import ci.nsu.mobile.main.Data.Models.UserDto
import ci.nsu.mobile.main.Network.ApiService

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            tokenManager.token = response.token
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clear()
    }
}