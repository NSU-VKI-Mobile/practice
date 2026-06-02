package ci.nsu.moble.main.data

import ci.nsu.moble.main.data.dto.GroupDto
import ci.nsu.moble.main.data.dto.RegisterRequest
import ci.nsu.moble.main.data.dto.UserDto
import ci.nsu.moble.main.data.dto.UserLoginRequestDto

class AuthRepository(
    private val api: ApiService = RetrofitClient.api
) {

    suspend fun login(login: String, password: String): Result<UserDto?> {
        return try {
            val response = api.login(UserLoginRequestDto(login, password))

            if (response.isSuccessful) {
                val body = response.body()
                TokenManager.token = body?.token
                Result.success(body?.user)
            } else {
                Result.failure(Exception("Ошибка входа: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(registerRequest)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка регистрации: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Ошибка получения пользователей: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Ошибка получения групп: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        TokenManager.clearToken()
    }
}