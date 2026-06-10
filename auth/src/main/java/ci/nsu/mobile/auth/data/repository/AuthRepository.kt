package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.data.api.ApiService
import ci.nsu.mobile.auth.data.model.dto.GroupDto
import ci.nsu.mobile.auth.data.model.dto.UserDto
import ci.nsu.mobile.auth.data.model.request.LoginRequest
import ci.nsu.mobile.auth.data.model.request.RegisterRequest

class AuthRepository(private val api: ApiService) {

    suspend fun login(login: String, password: String): Result<Unit> =
        runCatching {
            val response = api.login(LoginRequest(login, password))
            TokenManager.token = response["token"]
        }

    suspend fun register(request: RegisterRequest): Result<Unit> =
        runCatching { api.register(request) }

    suspend fun getUsers(): Result<List<UserDto>> =
        runCatching { api.getUsers() }

    suspend fun getGroups(): Result<List<GroupDto>> =
        runCatching { api.getGroups() }
}