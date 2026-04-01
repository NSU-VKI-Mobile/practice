package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.remote.ApiClient
import ci.nsu.mobile.main.data.local.TokenManager

class AuthRepository {
    private val api = ApiClient.authApi

    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = api.login(LoginRequest(login, password))
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    // Сохраняем токен из AuthResponse
                    TokenManager.token = authResponse.token
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Пустой ответ от сервера"))
                }
            } else {
                Result.failure(Exception("Ошибка входа: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети или парсинга: ${e.message}"))
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
                Result.failure(Exception("Ошибка загрузки пользователей"))
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
                Result.failure(Exception("Ошибка загрузки групп"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}