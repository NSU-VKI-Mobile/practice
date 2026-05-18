package ci.nsu.mobile.main.Repository

import RegisterRequest
import ci.nsu.mobile.main.Auth.TokenManager
import ci.nsu.mobile.main.Data.Models.GroupDto
import ci.nsu.mobile.main.Data.Models.PersonDto
import ci.nsu.mobile.main.Data.Models.UserDto
import ci.nsu.mobile.main.Network.ApiService
import ci.nsu.mobile.main.Network.LoginRequest

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            tokenManager.token = response.token
            // Для получения данных пользователя нужно сделать запрос /users/me - но по заданию его нет.
            // Можно вернуть пустой Result.success, либо сделать дополнительный вызов.
            Result.success(UserDto(0, login, "", "", PersonDto("", "", null, "", "", 0)))
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
        tokenManager.clear()
    }
}