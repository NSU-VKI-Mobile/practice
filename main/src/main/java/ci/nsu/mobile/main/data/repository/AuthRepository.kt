package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.remote.RetrofitClient
import ci.nsu.mobile.main.data.remote.TokenManager
import ci.nsu.mobile.main.data.remote.UserManager
import ci.nsu.mobile.main.data.remote.api.AuthApi
import ci.nsu.mobile.main.data.remote.model.GroupDto
import ci.nsu.mobile.main.data.remote.model.RegisterRequest
import ci.nsu.mobile.main.data.remote.model.UserDto
import ci.nsu.mobile.main.util.Result

class AuthRepository (
    private val tokenManager: TokenManager,
    private val userManager: UserManager

) {

    private val api: AuthApi = RetrofitClient.instance.create(AuthApi::class.java)

    suspend fun login(login: String, password: String): Result<String> {
        return try {
            val credentials = mapOf("login" to login, "password" to password)
            val response = api.login(credentials)
            TokenManager.token = response.token

            // Получаем ID пользователя и сохраняем
            val users = api.getUsers()
            val currentUser = users.find { it.login == login }
            currentUser?.let { user ->
                user.id?.let {id->
                UserManager.userId = id.toLong() }}

            Result.Success(response.token)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            api.register(request)
            Result.Success(Unit)
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