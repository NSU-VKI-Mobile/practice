package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.RetrofitClient
import ci.nsu.mobile.main.data.datasource.local.TokenManager
import ci.nsu.mobile.main.data.dto.LoginRequest
import ci.nsu.mobile.main.data.dto.RegisterRequest
import ci.nsu.mobile.main.data.model.Result
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val tokenManager: TokenManager
) {
    private val apiService = RetrofitClient.getApiService(tokenManager)

    suspend fun login(login: String, password: String): Result<String> {
        return try {
            val response = apiService.login(LoginRequest(login, password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    // Save token
                    tokenManager.token = loginResponse.token
                    saveUserIdFromToken()
                    Result.Success(loginResponse.token)
                } else {
                    Result.Error("Empty response from server")
                }
            } else {
                // Handle error based on HTTP code
                val errorMessage = when (response.code()) {
                    401 -> "Неверный логин или пароль"
                    400 -> "Неверный формат запроса"
                    else -> "Ошибка сервера: ${response.code()}"
                }
                Result.Error(errorMessage, response.code())
            }
        } catch (e: IOException) {
            Result.Error("Ошибка сети: проверьте подключение")
        } catch (e: HttpException) {
            Result.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            Result.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = apiService.register(request)

            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null && registerResponse.token != null) {
                    tokenManager.token = registerResponse.token
                    saveUserIdFromToken()
                }
                Result.Success(Unit)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Неверные данные регистрации"
                    409 -> "Пользователь с таким логином или email уже существует"
                    else -> "Ошибка регистрации: ${response.code()}"
                }
                Result.Error(errorMessage, response.code())
            }
        } catch (e: IOException) {
            Result.Error("Ошибка сети: проверьте подключение")
        } catch (e: HttpException) {
            Result.Error("Ошибка сервера: ${e.code()}")
        } catch (e: Exception) {
            Result.Error("Неизвестная ошибка: ${e.message}")
        }
    }

    private suspend fun saveUserIdFromToken() {
        try {
            val login = tokenManager.getUserLoginFromToken()

            if (login != null) {
                val userResponse = apiService.getUserByLogin(login)
                if (userResponse.isSuccessful && userResponse.body() != null) {
                    val user = userResponse.body()!!
                    tokenManager.userId = user.id
                }
            }
        } catch (e: Exception) {
            // Лог
            e.printStackTrace()
        }
    }

    fun logout() {
        tokenManager.clear()
    }
}