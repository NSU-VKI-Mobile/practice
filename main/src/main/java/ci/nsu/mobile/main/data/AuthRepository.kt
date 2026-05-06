package ci.nsu.mobile.main.data

import ci.nsu.mobile.main.data.network.AuthApi
import ci.nsu.mobile.main.data.network.GroupDto
import ci.nsu.mobile.main.data.network.LoginRequest
import ci.nsu.mobile.main.data.network.RegisterRequest
import ci.nsu.mobile.main.data.network.UserDto

class AuthRepository(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<UserDto> = runCatching {
        val response = api.login(LoginRequest(login, password))
        if (!response.isSuccessful) {
            error("Ошибка входа: ${response.code()}")
        }
        val user = response.body() ?: error("Пустой ответ сервера")
        user.token?.let { tokenManager.token = it }
        user
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> = runCatching {
        val response = api.register(registerRequest)
        if (!response.isSuccessful) {
            error("Ошибка регистрации: ${response.code()}")
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> = runCatching {
        val response = api.getUsers()
        if (!response.isSuccessful) {
            error("Не удалось получить пользователей: ${response.code()}")
        }
        response.body().orEmpty()
    }

    suspend fun getGroups(): Result<List<GroupDto>> = runCatching {
        val response = api.getGroups()
        if (!response.isSuccessful) {
            error("Не удалось получить группы: ${response.code()}")
        }
        response.body().orEmpty()
    }

    fun logout() {
        tokenManager.clear()
    }
}
