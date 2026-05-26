package ci.nsu.mobile.auth.data.repository

import ci.nsu.mobile.auth.data.api.AuthApiService
import ci.nsu.mobile.auth.data.dto.GroupDto
import ci.nsu.mobile.auth.data.model.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val apiService: AuthApiService
) {

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = apiService.getGroups()

            if (response.isSuccessful) {
                val groups = response.body()
                if (groups != null) {
                    Result.Success(groups)
                } else {
                    Result.Error("Пустой ответ от сервера")
                }
            } else {
                Result.Error("Ошибка загрузки групп: ${response.code()}")
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