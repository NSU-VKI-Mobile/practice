package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.model.*
import ci.nsu.mobile.main.network.ApiService

class AuthRepository(private val api: ApiService) {

    suspend fun login(login: String, password: String): Result<UserDto> {
        return try {
            val response = api.login(LoginRequest(login, password))

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка входа: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)

            if (response.isSuccessful) {
                Result.success(Unit)
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

            if (response.isSuccessful) {

                Result.success(
                    response.body() ?: emptyList()
                )

            } else {

                Result.failure(
                    Exception(
                        "Ошибка ${response.code()} : ${response.message()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {

            val response = api.getGroups()

            println("CODE = ${response.code()}")
            println("BODY = ${response.body()}")

            if (response.isSuccessful) {

                val body = response.body()

                Result.success(body ?: emptyList())
            } else {
                Result.failure(Exception("Ошибка групп: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}