package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.*

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun register(username: String, password: String, email: String): Result<Unit>
    suspend fun getUsers(): Result<List<User>>

    fun saveToken(token: String)
    fun getToken(): String?
    fun saveUserId(userId: Long)
    fun getUserId(): Long?
    fun logout()
}