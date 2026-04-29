package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.TokenManager
import ci.nsu.mobile.main.data.models.LoginRequest
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val apiService: ApiService
) {

    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            TokenManager.token = response["token"]
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.message}"))
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(request)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.message}"))
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.message}"))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Result.success(groups)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.message}"))
        }
    }

    fun logout() {
        TokenManager.clear()
    }

    fun isAuthenticated(): Boolean = TokenManager.token != null
}