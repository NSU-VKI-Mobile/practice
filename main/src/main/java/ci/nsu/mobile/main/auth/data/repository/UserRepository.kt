package ci.nsu.mobile.main.auth.data.repository

import ci.nsu.mobile.main.auth.data.api.ApiService
import ci.nsu.mobile.main.auth.data.dto.UserDto
import ci.nsu.mobile.main.auth.data.model.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = apiService.getUsers()

            if (response.isSuccessful) {
                val users = response.body()
                if (users != null) {
                    Result.Success(users)
                } else {
                    Result.Error("Пустой ответ от сервера")
                }
            } else {
                Result.Error("Ошибка загрузки пользователей: ${response.code()}")
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