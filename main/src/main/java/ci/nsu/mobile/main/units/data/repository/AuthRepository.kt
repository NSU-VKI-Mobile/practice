package ci.nsu.mobile.main.units.data.repository

import ci.nsu.mobile.main.units.data.api.ApiService
import ci.nsu.mobile.main.units.data.api.LoginResponse
import ci.nsu.mobile.main.units.data.model.*
import ci.nsu.mobile.main.units.data.token.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response: LoginResponse = apiService.login(LoginRequest(login, password))
            tokenManager.token = response.token
            Result.success(UserDto(id = 0, login = login, email = "", phoneNumber = "", roleId = 1))
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
            val users: List<UserDto> = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups: List<GroupDto> = apiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clearToken()
    }
}