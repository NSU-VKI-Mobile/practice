package ci.nsu.mobile.main.domain.repository

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.remote.RetrofitClient
import ci.nsu.mobile.main.data.remote.TokenManager
import ci.nsu.mobile.main.data.remote.api.AuthApi
import ci.nsu.mobile.main.util.Result

class AuthRepository {

    private val api: AuthApi = RetrofitClient.instance.create(AuthApi::class.java)



    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            api.register(request)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun login(login: String, password: String): Result<String> {
        return try {
            val credentials = mapOf("login" to login, "password" to password)
            val response = api.login(credentials)
            TokenManager.token = response.token
            Result.Success(response.token)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            Result.Success(api.getUsers())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            Result.Success(api.getGroups())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}