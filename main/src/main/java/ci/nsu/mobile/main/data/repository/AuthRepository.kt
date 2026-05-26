package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.*

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<LoginResponse>

    suspend fun register(request: RegisterRequest): Result<Unit>

    suspend fun getUsers(): Result<List<User>>

    fun saveToken(token: String)
    fun getToken(): String?
    fun saveUserId(userId: Long)
    fun getUserId(): Long?
    fun logout()
}