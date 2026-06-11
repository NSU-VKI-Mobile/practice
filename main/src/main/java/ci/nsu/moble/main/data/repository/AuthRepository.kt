package ci.nsu.moble.main.data.repository

import ci.nsu.moble.main.data.api.ApiService
import ci.nsu.moble.main.data.dto.*
import ci.nsu.moble.main.data.storage.TokenManager

class AuthRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<LoginResponse> = try {
        val response = api.login(LoginRequest(login, password))
        tokenManager.token = response.token
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun register(request: RegisterRequest): Result<Unit> = try {
        api.register(request)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUsers(): Result<List<UserDto>> = try {
        Result.success(api.getUsers())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getGroups(): Result<List<GroupDto>> = try {
        Result.success(api.getGroups())
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun logout() {
        tokenManager.clear()
    }
}