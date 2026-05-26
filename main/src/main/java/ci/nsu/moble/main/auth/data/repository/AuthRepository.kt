package ci.nsu.moble.main.auth.data.repository

import android.content.Context
import ci.nsu.moble.main.auth.data.models.*
import ci.nsu.moble.main.auth.data.network.ApiService  // ← этот импорт был пропущен
import ci.nsu.moble.main.auth.data.network.NetworkModule
import ci.nsu.moble.main.auth.utils.TokenManager
import retrofit2.HttpException
import java.io.IOException

// результат запроса (успех, ошибка, загрузка)
sealed class AuthApiResult<out T> {
    data class Success<T>(val data: T) : AuthApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : AuthApiResult<Nothing>()
    object Loading : AuthApiResult<Nothing>()
}

class AuthRepository(private val context: Context) {

    private val tokenManager = TokenManager(context)
    private var currentToken: String? = null

    // создаём клиент с токеном (или без)
    private fun getApiService(token: String? = currentToken): ApiService {
        val client = NetworkModule.provideOkHttpClient(token)
        val retrofit = NetworkModule.provideRetrofit(client)
        return NetworkModule.provideApiService(retrofit)
    }

    // вход: получаем токен от сервера
    suspend fun login(login: String, password: String): AuthApiResult<AuthResponse> {
        return try {
            val apiService = getApiService(null)
            val response = apiService.login(LoginRequest(login, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                currentToken = authResponse.token
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
        currentToken = null
        tokenManager.clearToken()
    }
}