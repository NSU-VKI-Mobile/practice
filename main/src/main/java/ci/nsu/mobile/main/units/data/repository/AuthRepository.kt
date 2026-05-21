package ci.nsu.mobile.main.units.data.repository

import ci.nsu.mobile.main.units.data.api.ApiService
import ci.nsu.mobile.main.units.data.model.GroupDto
import ci.nsu.mobile.main.units.data.model.LoginRequest
import ci.nsu.mobile.main.units.data.model.RegisterRequest
import ci.nsu.mobile.main.units.data.model.UserDto
import ci.nsu.mobile.main.units.data.token.TokenManager
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(login: String, password: String): Result<UserDto>{
        return try {
            val response = apiService.login(LoginRequest(login, password))
            tokenManager.token = response.token
            Result.success(UserDto(id= 0, login = login, email = "", phoneNumber = ""))
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.code()}"))
        }
    }
    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(request)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка сервера: ${e.code()}"))
        }
    }
    suspend fun getUsers(): Result<List<UserDto>>{
        return try {
            val response = apiService.getUsers()
            Result.success(users)
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
            Result.success(groups)
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
        tokenManager.clearToken()
    }
}