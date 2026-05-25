package ci.nsu.mobile.main.data.repository

import android.content.Context
import android.content.SharedPreferences
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.data.remote.ApiService

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context
) : AuthRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            saveToken(response.token)
            saveUserId(response.userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, password: String, email: String): Result<Unit> {
        return try {
            apiService.register(RegisterRequest(username, password, email))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    override fun getToken(): String? = prefs.getString("token", null)

    override fun saveUserId(userId: Long) {
        prefs.edit().putLong("userId", userId).apply()
    }

    override fun getUserId(): Long? {
        return if (prefs.contains("userId")) prefs.getLong("userId", -1) else null
    }

    override fun logout() {
        prefs.edit().clear().apply()
    }
}