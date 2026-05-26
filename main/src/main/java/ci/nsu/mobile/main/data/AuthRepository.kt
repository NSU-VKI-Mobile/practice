// ВАЖНО! Убедитесь, что строка package правильная!
package ci.nsu.mobile.main.data

import ci.nsu.mobile.main.data.models.* // <-- Импорт DTO
import ci.nsu.mobile.main.data.network.ApiService // <-- Импорт ApiService

class AuthRepository(private val apiService: ApiService) {

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(registerRequest)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(login: String, password: String): Result<UserDto?> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            TokenManager.token = response.token
            Result.success(null)
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
        TokenManager.clear()
    }
}