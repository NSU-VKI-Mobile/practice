package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.data.dto.AuthResponse
import ci.nsu.mobile.auth.data.dto.GroupDto
import ci.nsu.mobile.auth.data.dto.LoginRequest
import ci.nsu.mobile.auth.data.dto.RegisterRequest
import ci.nsu.mobile.auth.data.dto.UserDto
import ci.nsu.mobile.auth.data.network.RetrofitClient

class AuthRepository {
    suspend fun login(login: String, pass: String): Result<AuthResponse> =
        runCatching { RetrofitClient.api.login(LoginRequest(login, pass)) }

    suspend fun register(req: RegisterRequest): Result<Unit> =
        runCatching { RetrofitClient.api.register(req) }

    suspend fun getUsers(): Result<List<UserDto>> =
        runCatching { RetrofitClient.api.getUsers() }

    suspend fun getGroups(): Result<List<GroupDto>> =
        runCatching { RetrofitClient.api.getGroups() }
}