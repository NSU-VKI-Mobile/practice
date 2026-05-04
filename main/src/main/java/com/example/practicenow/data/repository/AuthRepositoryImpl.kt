package com.example.practicenow.data.repository

import com.example.practicenow.data.api.RetrofitClient
import com.example.practicenow.data.local.TokenManager
import com.example.practicenow.data.model.*
import com.example.practicenow.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    private val api = RetrofitClient.apiService

    override suspend fun login(loginRequest: LoginRequest): Result<UserDto> {
        return try {
            val response = api.login(loginRequest)
            TokenManager.token = response.token
            Result.success(UserDto(0, loginRequest.login, "", ""))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            api.register(registerRequest)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            Result.success(api.getUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            Result.success(api.getGroups())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}