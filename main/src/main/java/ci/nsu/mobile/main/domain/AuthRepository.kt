package ci.nsu.mobile.main.domain

import ci.nsu.mobile.main.data.models.*
import ci.nsu.mobile.main.data.network.ApiService
import ci.nsu.mobile.main.data.network.PublicApiService
import ci.nsu.mobile.main.data.storage.TokenManager
import ci.nsu.mobile.main.data.network.NetworkModule
class AuthRepository(
    private val apiService: ApiService,
    private val publicApiService: PublicApiService
) {

    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = publicApiService.login(LoginRequest(login, password))
            TokenManager.token = response.token
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            publicApiService.register(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            Result.success(publicApiService.getGroups())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            Result.success(apiService.getUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}