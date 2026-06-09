package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.data.*
import ci.nsu.mobile.main.network.ApiService
import ci.nsu.mobile.main.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    private val _authState = MutableStateFlow<Boolean?>(null)
    val authState: StateFlow<Boolean?> = _authState.asStateFlow()

    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            tokenManager.token = response.token
            _authState.value = true
            Result.Success(Unit)
        } catch (e: retrofit2.HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Неверный логин или пароль"
                404 -> "Пользователь не найден"
                else -> "Ошибка сервера: ${e.message()}"
            }
            Result.Error(errorMessage, e.code())
        } catch (e: IOException) {
            Result.Error("Ошибка сети: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Ошибка: ${e.message}")
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(registerRequest)
            Result.Success(Unit)
        } catch (e: retrofit2.HttpException) {
            val errorMessage = when (e.code()) {
                400 -> "Неверные данные регистрации"
                409 -> "Пользователь с таким логином или email уже существует"
                else -> "Ошибка сервера: ${e.message()}"
            }
            Result.Error(errorMessage, e.code())
        } catch (e: IOException) {
            Result.Error("Ошибка сети: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Ошибка: ${e.message}")
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Result.Success(users)
        } catch (e: retrofit2.HttpException) {
            Result.Error("Ошибка загрузки пользователей: ${e.code()}", e.code())
        } catch (e: IOException) {
            Result.Error("Ошибка сети: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Ошибка: ${e.message}")
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Result.Success(groups)
        } catch (e: retrofit2.HttpException) {
            Result.Error("Ошибка загрузки групп: ${e.code()}", e.code())
        } catch (e: IOException) {
            Result.Error("Ошибка сети: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Ошибка: ${e.message}")
        }
    }

    fun logout() {
        tokenManager.clear()
        _authState.value = false
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}