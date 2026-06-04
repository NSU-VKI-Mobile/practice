package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.LoginRequest
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.network.ApiClient
import ci.nsu.mobile.main.data.token.TokenManager

class AuthRepository {

    private val api = ApiClient.apiService

    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response = api.login(LoginRequest(login, password))
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    TokenManager.token = token
                    val userResponse = api.getCurrentUser()
                    if (userResponse.isSuccessful && userResponse.body() != null) {
                        Result.success(userResponse.body()!!)
                    } else {
                        val usersResponse = api.getUsers()
                        val user = usersResponse.body()?.find { it.login == login }
                        if (user != null) Result.success(user)
                        else Result.failure(Exception("Пользователь не найден"))
                    }
                } else {
                    Result.failure(Exception("Токен не получен"))
                }
            } else {
                Result.failure(Exception("Ошибка входа: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка регистрации: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Ошибка загрузки пользователей: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Ошибка загрузки групп: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
