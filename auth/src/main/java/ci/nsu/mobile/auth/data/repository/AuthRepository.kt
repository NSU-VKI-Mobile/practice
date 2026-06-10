package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.data.models.*
import ci.nsu.mobile.auth.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val apiService: ApiService
) {
    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            TokenManager.token = response["token"]
            TokenManager.userLogin = login

            val users = apiService.getUsers()
            val user = users.find { it.login == login }
            TokenManager.userId = user?.userId?.toLong()

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