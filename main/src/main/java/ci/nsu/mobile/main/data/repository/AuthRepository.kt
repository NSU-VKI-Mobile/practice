package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.RetrofitClient
import ci.nsu.mobile.main.data.datasource.local.TokenManager
import ci.nsu.mobile.main.data.dto.LoginRequestDto
import ci.nsu.mobile.main.data.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val tokenManager: TokenManager
) {
    private val apiService = RetrofitClient.getApiService(tokenManager)

    suspend fun login(login: String, password: String): Result<String> {
        return try {
            val response = apiService.login(LoginRequestDto(login, password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    // Save token
                    tokenManager.token = loginResponse.token
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
}