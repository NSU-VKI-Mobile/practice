package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.ApiService
import ci.nsu.mobile.main.data.model.dto.*
import ci.nsu.mobile.main.data.model.request.*
import ci.nsu.mobile.main.TokenManager

class AuthRepository(private val api: ApiService) {

    suspend fun login(login: String, password: String): Result<Unit> =
        runCatching {
            val response = api.login(LoginRequest(login, password))
            TokenManager.token = response["token"]
        }

    suspend fun register(request: RegisterRequest): Result<Unit> =
        runCatching {
            api.register(request)
        }

    suspend fun getUsers(): Result<List<UserDto>> =
        runCatching {
            api.getUsers()
        }

    suspend fun getGroups(): Result<List<GroupDto>> =
        runCatching {
            api.getGroups()
        }
}