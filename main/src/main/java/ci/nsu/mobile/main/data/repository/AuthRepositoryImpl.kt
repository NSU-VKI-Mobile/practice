package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.remote.api.ApiService
import ci.nsu.mobile.main.data.remote.dto.*
import ci.nsu.mobile.main.domain.repository.AuthRepository
import ci.nsu.mobile.main.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(LoginRequest(login, password))
                tokenManager.token = response.token
                Result.Success(response)
            } catch (_: UnknownHostException) {
                Result.Error("Нет подключения к сети")
            } catch (e: HttpException) {
                Result.Error("Неверный логин или пароль", e.code())
            } catch (e: Exception) {
                Result.Error("Ошибка: ${e.message}")
            }
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.register(request)
                Result.Success(Unit)
            } catch (_: UnknownHostException) {
                Result.Error("Нет подключения к сети")
            } catch (e: HttpException) {
                Result.Error("Ошибка регистрации: ${e.message}", e.code())
            } catch (e: Exception) {
                Result.Error("Ошибка: ${e.message}")
            }
        }
    }

    override suspend fun getUsers(): Result<List<UserDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val users = apiService.getUsers()
                Result.Success(users)
            } catch (_: UnknownHostException) {
                Result.Error("Нет подключения к сети")
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    tokenManager.clear()
                    Result.Error("Сессия истекла. Войдите заново.")
                } else {
                    Result.Error("Ошибка загрузки пользователей", e.code())
                }
            } catch (e: Exception) {
                Result.Error("Ошибка: ${e.message}")
            }
        }
    }

    override suspend fun getGroups(): Result<List<GroupDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val groups = apiService.getGroups()
                Result.Success(groups)
            } catch (_: UnknownHostException) {
                Result.Error("Нет подключения к сети")
            } catch (e: HttpException) {
                Result.Error("Ошибка загрузки групп", e.code())
            } catch (e: Exception) {
                Result.Error("Ошибка: ${e.message}")
            }
        }
    }
}