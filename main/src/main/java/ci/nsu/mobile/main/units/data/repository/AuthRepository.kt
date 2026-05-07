package ci.nsu.mobile.main.units.data.repository

import ci.nsu.mobile.main.units.data.api.ApiService
import ci.nsu.mobile.main.units.data.model.GroupDto
import ci.nsu.mobile.main.units.data.model.LoginRequest
import ci.nsu.mobile.main.units.data.model.RegisterRequest
import ci.nsu.mobile.main.units.data.model.UserDto
import ci.nsu.mobile.main.units.data.token.TokenManager
import okio.IOException
import retrofit2.HttpException

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<UserDto>{
        return try {
            val response = apiService.login(LoginRequest(login, password))
            if (response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка входа: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.code()}"))
        }
    }
    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = apiService.register(request)
            if(response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка регистрации: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.code()}"))
        }
    }
    suspend fun getUsers(): Result<List<UserDto>>{
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка загрузки пользователей."))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.code()}"))
        }
    }
    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = apiService.getGroups()
            if(response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка загрузки групп."))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.code()}"))
        }
    }
    fun saveToken(token: String){
        tokenManager.token = token
    }
    fun logout() {
        tokenManager.clear()
    }
}