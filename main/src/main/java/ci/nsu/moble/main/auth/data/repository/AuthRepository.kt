package ci.nsu.moble.main.auth.data.repository

import android.content.Context
import ci.nsu.moble.main.auth.data.models.*
import ci.nsu.moble.main.auth.data.network.NetworkModule
import ci.nsu.moble.main.auth.utils.TokenManager
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val context: Context) {

    private val tokenManager = TokenManager(context)
    private val apiService = NetworkModule.provideApiService(
        NetworkModule.provideRetrofit(
            NetworkModule.provideOkHttpClient(tokenManager)
        )
    )

    suspend fun register(request: RegisterRequest): AuthApiResult<Unit> {
        return try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                AuthApiResult.Success(Unit)
            } else {
                AuthApiResult.Error("Ошибка регистрации: ${response.code()}")
            }
        } catch (e: IOException) {
            AuthApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            AuthApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            AuthApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun login(login: String, password: String): AuthApiResult<AuthResponse> {
        return try {
            val request = LoginRequest(login, password)
            val response = apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                tokenManager.saveToken(authResponse.token)
                AuthApiResult.Success(authResponse)
            } else {
                AuthApiResult.Error("Неверный логин или пароль")
            }
        } catch (e: IOException) {
            AuthApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            AuthApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            AuthApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun getGroups(): AuthApiResult<List<GroupDto>> {
        return try {
            val response = apiService.getGroups()
            if (response.isSuccessful && response.body() != null) {
                AuthApiResult.Success(response.body()!!)
            } else {
                AuthApiResult.Error("Ошибка загрузки групп: ${response.code()}")
            }
        } catch (e: IOException) {
            AuthApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            AuthApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            AuthApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun getUsers(): AuthApiResult<List<UserDto>> {
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                AuthApiResult.Success(response.body()!!)
            } else if (response.code() == 401) {
                AuthApiResult.Error("Сессия истекла, войдите заново", code = 401)
            } else {
                AuthApiResult.Error("Ошибка загрузки пользователей: ${response.code()}")
            }
        } catch (e: IOException) {
            AuthApiResult.Error("Нет соединения с сервером")
        } catch (e: HttpException) {
            AuthApiResult.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            AuthApiResult.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun logout() {
        tokenManager.clearToken()
    }

    suspend fun isAuthenticated(): Boolean {
        val token = tokenManager.getToken()
        return !token.isNullOrEmpty()
    }
}