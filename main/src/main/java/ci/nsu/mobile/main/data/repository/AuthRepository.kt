package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.network.api.ApiService
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.AuthResponse
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegistrationRequest
import ci.nsu.mobile.main.data.model.UserDto

class AuthRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            tokenManager.saveToken(response.token)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        request: RegistrationRequest
    ): Result<AuthResponse> {
        return try {
            val response = api.register(request)
            tokenManager.saveToken(response.token)

            Result.success(response)
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
}