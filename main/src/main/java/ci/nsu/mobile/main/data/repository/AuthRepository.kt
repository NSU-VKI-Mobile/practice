package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.LoginResponse
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.network.NetworkClient
import ci.nsu.mobile.main.data.security.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class AuthRepository {
    private val apiService = NetworkClient.apiService
    private val tokenManager = TokenManager

    suspend fun login(login: String, password: String): Result<UserDto> = withContext(Dispatchers.IO) {
        try {
            // 1. Получаем токен
            val tokenResponse = apiService.login(LoginRequest(login, password))
            tokenManager.token = tokenResponse.token

            // 2. Получаем список всех пользователей
            val users = apiService.getUsers()

            // 3. Находим текущего пользователя по логину
            val currentUser = users.find { it.login == login }

            if (currentUser != null) {
                Result.success(currentUser)
            } else {
                Result.failure(IOException("Пользователь не найден в списке"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.register(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> = withContext(Dispatchers.IO) {
        try {
            val groups = apiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(IOException("Ошибка загрузки групп: ${e.message}"))
        }
    }
}