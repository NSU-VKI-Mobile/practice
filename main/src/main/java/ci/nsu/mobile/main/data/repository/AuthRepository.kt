package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.*

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<Unit>
    suspend fun register(request: RegisterRequest): Result<Unit>
    suspend fun getUsers(): Result<List<UserDto>>
    suspend fun getGroups(): Result<List<GroupDto>>
    fun logout()
    fun isLoggedIn(): Boolean
    fun getUserId(): Long?
}