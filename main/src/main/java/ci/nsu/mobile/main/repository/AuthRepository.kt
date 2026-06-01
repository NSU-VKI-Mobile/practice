package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.network.RetrofitInstance

class AuthRepository {

    suspend fun login(
        login: String,
        password: String
    ): Result<UserDto> {

        return try {

            val response =
                RetrofitInstance.api.login(
                    LoginRequest(login, password)
                )

            if (response.isSuccessful) {

                val body = response.body()!!

                TokenManager.token = body.token

                Result.success(body.user)

            } else {
                Result.failure(Exception("Login failed"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<Unit> {

        return try {

            val response =
                RetrofitInstance.api.register(request)

            if (response.isSuccessful)
                Result.success(Unit)
            else
                Result.failure(Exception())

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers() =
        Result.success(
            RetrofitInstance.api.getUsers().body() ?: emptyList()
        )

    suspend fun getGroups() =
        Result.success(
            RetrofitInstance.api.getGroups().body() ?: emptyList()
        )
}