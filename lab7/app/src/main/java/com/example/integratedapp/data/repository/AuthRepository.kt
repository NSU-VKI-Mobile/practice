package com.example.integratedapp.data.repository

import com.example.integratedapp.data.SessionManager
import com.example.integratedapp.data.api.RetrofitClient
import com.example.integratedapp.data.model.*

// Repository для авторизации — работает с REST API
class AuthRepository {

    private val api = RetrofitClient.apiService

    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response = api.login(LoginRequest(login, password))
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                // Сохраняем данные сессии
                user.token?.let { SessionManager.token = it }
                user.id?.let { SessionManager.userId = it }
                SessionManager.userLogin = user.login
                Result.success(user)
            } else {
                Result.failure(Exception("Ошибка входа: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка регистрации: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else Result.failure(Exception("Ошибка загрузки групп"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }
}
