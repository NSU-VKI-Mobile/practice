package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.RetrofitClient
import ci.nsu.mobile.main.data.datasource.local.TokenManager
import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.data.model.Result
import retrofit2.HttpException
import java.io.IOException

class GroupRepository(
    private val tokenManager: TokenManager
) {
    private val apiService = RetrofitClient.getApiService(tokenManager)

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