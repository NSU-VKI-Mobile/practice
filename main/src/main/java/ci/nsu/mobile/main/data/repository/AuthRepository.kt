package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.data.dto.LoginRequest
import ci.nsu.mobile.main.data.dto.LoginResponse
import ci.nsu.mobile.main.data.dto.RegisterRequest
import ci.nsu.mobile.main.data.dto.UserDto
import ci.nsu.mobile.main.network.RetrofitClient

class AuthRepository {

    private val api = RetrofitClient.api

    suspend fun login(
        login: String,
        password: String
    ): Result<LoginResponse> {

        return try {

            Result.success(
                api.login(
                    LoginRequest(login, password)
                )
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<Unit> {

        return try {

            api.register(request)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getUsers()
            : Result<List<UserDto>> {

        return try {

            Result.success(api.getUsers())

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getGroups()
            : Result<List<GroupDto>> {

        return try {

            Result.success(api.getGroups())

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}