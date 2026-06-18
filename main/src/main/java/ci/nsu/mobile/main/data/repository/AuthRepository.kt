package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.data.network.RetrofitClient
import java.io.IOException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object Idle : Result<Nothing>() // Добавьте это
}

object AuthRepository {

    suspend fun login(login: String, password: String): Result<LoginResponse> {
        return try {
            val response = RetrofitClient.api.login(mapOf("login" to login, "password" to password))
            TokenManager.token = response.token // Сохраняем токен
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Ошибка сети")
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            RetrofitClient.api.register(request)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Ошибка регистрации")
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = RetrofitClient.api.getUsers()
            Result.Success(users)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Ошибка получения пользователей")
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = RetrofitClient.api.getGroups()
            Result.Success(groups)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Ошибка получения групп")
        }
    }

    fun logout() {
        TokenManager.clearToken()
    }
}