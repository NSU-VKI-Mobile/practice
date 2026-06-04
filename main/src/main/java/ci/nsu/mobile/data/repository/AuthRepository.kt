package ci.nsu.mobile.data.repository

import android.util.Log
import ci.nsu.mobile.data.model.*
import ci.nsu.mobile.data.remote.ApiService
import ci.nsu.mobile.data.remote.RetrofitClient

class AuthRepository {

    private var api: ApiService = RetrofitClient.createApi()

    fun resetApi() {
        api = RetrofitClient.createApi()
    }

    suspend fun login(
        login: String,
        password: String
    ): Result<AuthResponse> {

        return try {

            val response = api.login(
                mapOf(
                    "login" to login,
                    "password" to password
                )
            )

            if (response.isSuccessful && response.body() != null) {

                Result.success(response.body()!!)

            } else {

                Result.failure(
                    Exception("Ошибка входа ${response.code()}")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<AuthResponse> {

        return try {

            val response = api.register(request)

            if (response.isSuccessful && response.body() != null) {

                Result.success(response.body()!!)

            } else {

                Result.failure(Exception("Ошибка регистрации"))
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()
            Result.success(response.body() ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    data class AuthResponse(
        val token: String,
        val userId: Long
    )
}