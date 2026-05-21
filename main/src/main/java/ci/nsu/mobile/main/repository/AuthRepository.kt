package ci.nsu.mobile.main.repository

import android.util.Log
import ci.nsu.mobile.main.model.*
import ci.nsu.mobile.main.remote.ApiService
import ci.nsu.mobile.main.utils.TokenManager

class AuthRepository {

    private val api = ApiService()

    suspend fun login(
        login: String,
        password: String
    ): Result<UserDto?> {

        return try {

            val response = api.login(
                LoginRequest(login, password)
            )

            TokenManager.token = response.token

            Result.success(null)

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

    suspend fun getUsers(): Result<List<UserDto>> {

        return try {

            Result.success(api.getUsers())

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {

        return try {

            Result.success(api.getGroups())

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
    fun logout() {
        TokenManager.clear()
    }
}