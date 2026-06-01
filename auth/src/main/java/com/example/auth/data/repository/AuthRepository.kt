package com.example.auth.data.repository


import com.example.auth.data.network.ApiService
import com.example.auth.data.network.TokenManager
import com.example.auth.data.network.model.GroupDto
import com.example.auth.data.network.model.LoginRequest
import com.example.auth.data.network.model.RegisterRequest
import com.example.auth.data.network.model.toDomainUser
import com.example.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val service: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<User> {
        return try {
            val response = service.loginUser(LoginRequest(login = login, password = password))
            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception("Ошибка входа: ${response.code()} ${response.message()}"))
            }
            val authResponse = response.body()!!
            tokenManager.token = authResponse.token
            tokenManager.userLogin = login
            val usersResponse = service.getUsers()
            if (!usersResponse.isSuccessful || usersResponse.body() == null) {
                return Result.failure(Exception("Ошибка получения пользователей"))
            }
            val currentUser = usersResponse.body()!!.find { it.login == login }
            if (currentUser != null) {
                tokenManager.userId = currentUser.userId
                val user = currentUser.toDomainUser()
                Result.success(user)
            } else {
                tokenManager.clear()
                Result.failure(Exception("Пользователь не найден в системе"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<User> {
        return try {
            val response = service.registerUser(registerRequest)
            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception("Ошибка регистрации: ${response.code()} ${response.message()}"))
            }
            val authResponse = response.body()!!
            tokenManager.token = authResponse.token
            tokenManager.userLogin = registerRequest.login
            val usersResponse = service.getUsers()
            if (!usersResponse.isSuccessful || usersResponse.body() == null) {
                return Result.failure(Exception("Ошибка получения пользователей"))
            }
            val currentUser = usersResponse.body()!!.find { it.login == registerRequest.login }
            if (currentUser != null) {
                tokenManager.userId = currentUser.userId
                val user = currentUser.toDomainUser()
                Result.success(user)
            } else {
                tokenManager.clear()
                Result.failure(Exception("Пользователь не найден после регистрации"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
    }

    suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = service.getUsers()
            if (response.isSuccessful && response.body() != null) {
                val users = response.body()!!.map { it.toDomainUser() }
                Result.success(users)
            } else {
                Result.failure(Exception("Ошибка получения пользователей"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = service.getGroups()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка получения групп"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сетевая ошибка: ${e.message}", e))
        }
    }
}