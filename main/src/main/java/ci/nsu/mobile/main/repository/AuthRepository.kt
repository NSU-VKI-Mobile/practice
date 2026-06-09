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

                Result.failure(
                    Exception(
                        "LOGIN ERROR HTTP ${response.code()} : ${response.errorBody()?.string()}"
                    )
                )
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

            if (response.isSuccessful) {

                Result.success(Unit)

            } else {

                val error =
                    response.errorBody()?.string()

                Result.failure(
                    Exception(
                        "REGISTER ERROR HTTP ${response.code()} : $error"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {

        return try {

            val response = RetrofitInstance.api.getUsers()

            if (response.isSuccessful) {

                Result.success(response.body() ?: emptyList())

            } else {

                Result.failure(
                    Exception(
                        "GET USERS ERROR HTTP ${response.code()} : ${response.errorBody()?.string()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {

        return try {

            val response = RetrofitInstance.api.getGroups()

            if (response.isSuccessful) {

                Result.success(response.body() ?: emptyList())

            } else {

                Result.failure(
                    Exception(
                        "GET GROUPS ERROR HTTP ${response.code()} : ${response.errorBody()?.string()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}