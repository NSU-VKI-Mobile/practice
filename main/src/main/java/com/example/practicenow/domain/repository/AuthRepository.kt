package com.example.practicenow.domain.repository

import com.example.practicenow.data.model.*

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Result<UserDto>
    suspend fun register(registerRequest: RegisterRequest): Result<Unit>
    suspend fun getUsers(): Result<List<UserDto>>
    suspend fun getGroups(): Result<List<GroupDto>>
}