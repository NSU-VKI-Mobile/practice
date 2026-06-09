package ci.nsu.moble.main.data.repository

import android.content.Context
import ci.nsu.moble.main.data.models.*
import ci.nsu.moble.main.data.network.ApiService
import ci.nsu.moble.main.data.network.NetworkModule
import ci.nsu.moble.main.TokenManager
import retrofit2.HttpException
import java.io.IOException

sealed class AuthApiResult<out T> {
    data class Success<T>(val data: T) : AuthApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : AuthApiResult<Nothing>()
    object Loading : AuthApiResult<Nothing>()
}

class AuthRepository(private val context: Context) {

    private val tokenManager = TokenManager(context)

    private fun getApiService(): ApiService {
        val client = NetworkModule.provideOkHttpClient(tokenManager)
        val retrofit = NetworkModule.provideRetrofit(client)
        return NetworkModule.provideApiService(retrofit)
    }

    // вход: получаем токен от сервера
    suspend fun login(login: String, password: String): AuthApiResult<AuthResponse> {
        return try {
            val apiService = getApiService()
            val response = apiService.login(LoginRequest(login, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                tokenManager.saveToken(authResponse.token)
                AuthApiResult.Success(authResponse)
            } else {
                AuthApiResult.Error("неверный логин или пароль")
            }
        } catch (e: IOException) {
            AuthApiResult.Error("нет соединения с сервером")
        } catch (e: HttpException) {
            AuthApiResult.Error("ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            AuthApiResult.Error("неизвестная ошибка: ${e.message}")
        }
    }

    // регистрация: отправляем данные на сервер
    suspend fun register(request: RegisterRequest): AuthApiResult<Unit> {
        return try {
            val apiService = getApiService()
            val response = apiService.register(request)
            if (response.isSuccessful) {
                AuthApiResult.Success(Unit)
            } else {
                AuthApiResult.Error("ошибка регистрации: ${response.code()}")
            }
        } catch (e: IOException) {
            AuthApiResult.Error("нет соединения с сервером")
        } catch (e: Exception) {
            AuthApiResult.Error(e.message ?: "неизвестная ошибка")
        }
    }

    // получаем список пользователей (требует токен)
    suspend fun getUsers(): AuthApiResult<List<UserDto>> {
        return try {
            val apiService = getApiService()
            val response = apiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                AuthApiResult.Success(response.body()!!)
            } else if (response.code() == 401) {
                AuthApiResult.Error("сессия истекла, войдите заново", code = 401)
            } else {
                AuthApiResult.Error("ошибка загрузки пользователей: ${response.code()}")
            }
        } catch (e: IOException) {
            AuthApiResult.Error("нет соединения с сервером")
        } catch (e: Exception) {
            AuthApiResult.Error(e.message ?: "ошибка загрузки")
        }
    }

    // выход: забываем токен
    suspend fun logout() {
        tokenManager.clearToken()
    }
}