package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.network.NetworkModule
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.data.network.dto.GroupDto
import ci.nsu.mobile.main.data.network.dto.LoginRequest
import ci.nsu.mobile.main.data.network.dto.RegisterRequest
import ci.nsu.mobile.main.data.network.dto.UserDto

class AuthRepository {
    private val api = NetworkModule.apiService

    suspend fun login(login: String, password: String): Result<UserDto> = runCatching {
        val response = api.login(LoginRequest(login = login, password = password))
        if (!response.isSuccessful) {
            throw IllegalStateException("Ошибка входа: HTTP ${response.code()}")
        }

        val body = response.body() ?: throw IllegalStateException("Пустой ответ при входе")
        val token = body.token ?: body.accessToken ?: body.jwt
            ?: throw IllegalStateException("Токен не получен")
        TokenManager.token = token

        UserDto(login = login, email = null, id = null)
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> = runCatching {
        val response = api.register(registerRequest)
        if (!response.isSuccessful) {
            throw IllegalStateException("Ошибка регистрации: HTTP ${response.code()}")
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> = runCatching {
        val response = api.getUsers()
        if (!response.isSuccessful) {
            throw IllegalStateException("Ошибка получения пользователей: HTTP ${response.code()}")
        }
        response.body().orEmpty()
    }

    suspend fun getGroups(): Result<List<GroupDto>> = runCatching {
        val response = api.getGroups()
        if (!response.isSuccessful) {
            throw IllegalStateException("Ошибка получения групп: HTTP ${response.code()}")
        }
        response.body().orEmpty()
    }

    fun logout() {
        TokenManager.clear()
    }
}
