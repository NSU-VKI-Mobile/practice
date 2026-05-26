package ci.nsu.mobile.main.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.data.remote.ApiService

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context
) : AuthRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override suspend fun login(login: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            saveToken(response.token)

            val userResponse = apiService.getUserByLogin(login)
            saveUserId(userResponse.userId)

            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(request)
            Result.success(Unit)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            Result.success(apiService.getUsers())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getUserByLogin(login: String): Result<User> {
        return try {
            Result.success(apiService.getUserByLogin(login))
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override fun saveToken(token: String) {
        prefs.edit { putString("token", token) }
    }

    override fun getToken(): String? = prefs.getString("token", null)

    override fun saveUserId(userId: Long) {
        prefs.edit { putLong("userId", userId) }
    }

    override fun getUserId(): Long? {
        return if (prefs.contains("userId")) prefs.getLong("userId", -1) else null
    }

    override fun logout() {
        prefs.edit { clear() }
    }
}