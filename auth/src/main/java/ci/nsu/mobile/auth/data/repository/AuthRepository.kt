package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.data.models.AuthResponse
import ci.nsu.mobile.auth.data.models.LoginRequest
import ci.nsu.mobile.auth.data.models.RegisterRequest
import ci.nsu.mobile.auth.data.models.GroupDto
import ci.nsu.mobile.auth.data.models.UserDto
import ci.nsu.mobile.auth.data.network.NetworkModule
import ci.nsu.mobile.auth.utils.UserPreferences
import retrofit2.HttpException
import java.io.IOException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

class AuthRepository(
    private val userPreferences: UserPreferences
) {

    private val apiService = NetworkModule.provideApiService(
        NetworkModule.provideRetrofit(
            NetworkModule.provideOkHttpClient(userPreferences)
        )
    )

    suspend fun register(request: RegisterRequest): ApiResult<Unit> {
        return try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error("Ошибка регистрации: ${response.code()}")
            }
        } catch (e: IOException) {
            ApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            ApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            ApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun login(login: String, password: String): ApiResult<AuthResponse> {
        return try {
            val request = LoginRequest(login, password)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                userPreferences.saveToken(authResponse.token)
                // сохраняем userId как заглушку ибо сервер не возвращает id(?)
                userPreferences.saveUserId(1L)
                ApiResult.Success(authResponse)
            } else {
                ApiResult.Error("Неверный логин или пароль")
            }
        } catch (e: IOException) {
            ApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            ApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            ApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun getGroups(): ApiResult<List<GroupDto>> {
        return try {
            val response = apiService.getGroups()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error("Ошибка загрузки групп: ${response.code()}")
            }
        } catch (e: IOException) {
            ApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            ApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            ApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun getUsers(): ApiResult<List<UserDto>> {
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else if (response.code() == 401) {
                ApiResult.Error("Сессия истекла, войдите заново", code = 401)
            } else {
                ApiResult.Error("Ошибка загрузки пользователей: ${response.code()}")
            }
        } catch (e: IOException) {
            ApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            ApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            ApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun logout() {
        userPreferences.clear()
    }

    suspend fun isAuthenticated(): Boolean {
        val token = userPreferences.getToken()
        return !token.isNullOrEmpty()
    }
}