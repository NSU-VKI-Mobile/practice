package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.Api
import ci.nsu.mobile.main.data.api.TokenManager
import ci.nsu.mobile.main.data.model.*
import kotlinx.serialization.InternalSerializationApi
@OptIn(InternalSerializationApi::class)
class AuthRepository {
    private val api = Api.service

    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = api.login(LoginRequest(login, password))
            if (response.isSuccessful) {
                val token = response.body()?.token ?: ""
                TokenManager.token = token
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка входа: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}"))
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка регистрации: ${response.code()} ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}"))
        }
    }


    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка получения пользователей"))
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}"))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Ошибка получения групп"))
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}"))
        }
    }
}