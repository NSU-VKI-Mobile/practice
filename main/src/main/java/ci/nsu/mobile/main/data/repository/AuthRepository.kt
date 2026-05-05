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
    suspend fun register(registerRequest: RegisterRequest): Result<UserDto> {
        return try {
            val response = service.registerUser(registerRequest)
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                TokenManager.token = authResponse.token
                TokenManager.userLogin = registerRequest.login
                TokenManager.userId = authResponse.user.userId
                Result.success(authResponse.user)
            } else {
                Result.failure(Exception("error register"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = service.getUsers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("error get users"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = service.getGroups()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("error get groups"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}