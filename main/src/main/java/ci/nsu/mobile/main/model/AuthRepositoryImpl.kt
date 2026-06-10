package ci.nsu.mobile.main.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(login, password)
                val response = apiService.login(request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(Exception("Login failed: ${e.message}"))
            }
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Registration failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Registration failed: ${e.message}"))
            }
        }
    }

    override suspend fun getUsers(): Result<List<UserDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val users = apiService.getUsers()
                Result.success(users)
            } catch (e: Exception) {
                Result.failure(Exception("Failed to get users: ${e.message}"))
            }
        }
    }

    override suspend fun getGroups(): Result<List<GroupDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val groups = apiService.getGroups()
                Result.success(groups)
            } catch (e: Exception) {
                Result.failure(Exception("Failed to get groups: ${e.message}"))
            }
        }
    }
}