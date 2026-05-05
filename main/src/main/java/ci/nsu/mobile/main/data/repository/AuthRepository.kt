package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.network.AppClient
import retrofit2.Response

object AuthRepository {
    private val apiService = AppClient.apiService

    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            if (response.isSuccessful && response.body() != null) {
                TokenManager.saveToken(response.body()!!.token)
                Result.success(response.body()!!.user)
            } else {
                Result.failure(Exception(response.errorMessage("Не удалось выполнить вход")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorMessage("Не удалось зарегистрироваться")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorMessage("Не удалось загрузить список пользователей")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = apiService.getGroups()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorMessage("Не удалось загрузить список групп")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun Response<*>.errorMessage(defaultMessage: String): String {
    val responseBody = errorBody()?.string()?.trim().orEmpty()
    return buildString {
        append(defaultMessage)
        append(" (HTTP ")
        append(code())
        append(")")
        if (responseBody.isNotEmpty()) {
            append(": ")
            append(responseBody)
        }
    }
}
