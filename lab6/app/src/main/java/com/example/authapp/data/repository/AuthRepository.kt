package com.example.authapp.data.repository

import com.example.authapp.data.TokenManager
import com.example.authapp.data.api.RetrofitClient
import com.example.authapp.data.model.*

// AuthRepository — прослойка между ViewModel и сетью
// ViewModel говорит: "залогинь пользователя"
// Repository знает КАК это сделать (через Retrofit → OkHttp → HTTP → Сервер)
//
// Result<T> — стандартный Kotlin-тип: либо успех (Result.success), либо ошибка (Result.failure)

class AuthRepository {

    private val api = RetrofitClient.apiService

    // Вход в систему
    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            // Отправляем POST-запрос с логином и паролем
            val response = api.login(LoginRequest(login, password))

            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                // Сохраняем JWT-токен для будущих запросов
                user.token?.let { TokenManager.token = it }
                Result.success(user)
            } else {
                // Сервер ответил ошибкой (401 Unauthorized, 403 Forbidden и т.д.)
                Result.failure(Exception("Ошибка входа: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            // Нет интернета, сервер не отвечает и т.д.
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    // Регистрация нового пользователя
    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful) {
                Result.success(Unit) // Unit — "ничего", просто факт успеха
            } else {
                Result.failure(Exception("Ошибка регистрации: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    // Получить список пользователей (требует авторизации)
    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка загрузки: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    // Получить список групп (для выпадающего списка при регистрации)
    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка загрузки групп: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }
}
