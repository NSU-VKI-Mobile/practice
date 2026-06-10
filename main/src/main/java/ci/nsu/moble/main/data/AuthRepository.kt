// Task_6: Репозиторий аутентификации — прослойка между ViewModel и API.
package ci.nsu.moble.main.data

import ci.nsu.moble.main.data.remote.RetrofitClient
import ci.nsu.moble.main.data.remote.TokenManager
import ci.nsu.moble.main.data.remote.dto.GroupDto
import ci.nsu.moble.main.data.remote.dto.LoginRequest
import ci.nsu.moble.main.data.remote.dto.RegisterRequest
import ci.nsu.moble.main.data.remote.dto.UserDto

class AuthRepository {

    private val api = RetrofitClient.apiService

    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response = api.login(LoginRequest(login, password))
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    user.token?.let { TokenManager.token = it }
                    Result.success(user)
                } else {
                    Result.failure(Exception("Пустой ответ от сервера"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Ошибка входа (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Ошибка регистрации (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Ошибка получения пользователей (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Ошибка получения групп (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}
