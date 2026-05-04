package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.network.ApiService
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.data.network.model.GroupDto
import ci.nsu.mobile.main.data.network.model.LoginRequest
import ci.nsu.mobile.main.data.network.model.RegisterRequest
import ci.nsu.mobile.main.data.network.model.UserDto

class AuthRepository(
    private val service: ApiService
) {
    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response = service.loginUser(LoginRequest(login = login, password = password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                TokenManager.token = authResponse.token
                TokenManager.userLogin = login
                TokenManager.userId = authResponse.user.userId
                Result.success(authResponse.user)
            } else {
                Result.failure(Exception("error login"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun register(registerRequest: RegisterRequest): Result<Unit> {
    }
    fun getUsers(): Result<List<UserDto>> {
    }
    fun getGroups(): Result<List<GroupDto>> {
    }

}