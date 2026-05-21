package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.ApiService
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.token.TokenManager
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode


class AuthRepository {

    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val loginRequest = LoginRequest(login, password)
            val response = ApiService.login(loginRequest)
            TokenManager.token = response.token
            // After login, fetch user data
            val users = ApiService.getUsers()
            val currentUser = users.find { it.login == login }
            if (currentUser != null) {
                Result.success(currentUser)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val response: HttpResponse = ApiService.register(registerRequest)
            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                Result.success(Unit)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Registration failed: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = ApiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List< GroupDto>> {
        return try {
            val groups = ApiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}