package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.model.extractToken
import ci.nsu.mobile.main.data.model.toUserDto
import ci.nsu.mobile.main.data.network.AuthApi
import ci.nsu.mobile.main.data.network.TokenManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import android.util.Base64
import retrofit2.HttpException

class AuthRepository(
    private val api: AuthApi
) {
    suspend fun login(login: String, password: String): Result<UserDto> = runCatching {
        val response = api.login(LoginRequest(login = login, password = password))
        val user = response.toUserDto()
        val token = response.extractToken() ?: user.extractToken()
        if (token.isNullOrBlank()) {
            throw IllegalStateException("Сервер не вернул токен авторизации.")
        }
        TokenManager.token = token
        TokenManager.userId = user.userId?.toLong()
            ?: user.id?.toLong()
            ?: parseUserIdFromJwt(token)
        user
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> = runCatching {
        val response = api.register(registerRequest)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        Unit
    }

    suspend fun getUsers(): Result<List<UserDto>> = runCatching {
        api.getUsers()
    }

    suspend fun getGroups(): Result<List<GroupDto>> = runCatching {
        api.getGroups()
    }

    private fun parseUserIdFromJwt(token: String): Long? {
        val payload = token.split('.').getOrNull(1) ?: return null
        return runCatching {
            val decoded = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP))
            val json = Json.parseToJsonElement(decoded).jsonObject
            listOf("userId", "id", "sub")
                .firstNotNullOfOrNull { key ->
                    json[key]?.jsonPrimitive?.content?.toLongOrNull()
                }
        }.getOrNull()
    }
}
