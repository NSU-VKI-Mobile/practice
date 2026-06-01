package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            // 🟢 Явно выполняем сетевые запросы в IO потоке
            withContext(Dispatchers.IO) {
                val response = apiService.login(LoginRequest(login, password))
                TokenManager.token = response.token

                // Получаем ID пользователя. Если здесь ошибка, токен уже сохранен,
                // но мы можем считать это частичным успехом или откатить токен.
                // Для простоты оставим так, но добавим проверку на null userId
                val user = apiService.getUserByLogin(login)

                if (user.userId > 0) {
                    TokenManager.userId = user.userId
                } else {
                    // Если сервер вернул странного пользователя без ID
                    throw IllegalStateException("Received user has invalid ID")
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            // В случае ошибки очищаем токен, чтобы не осталось "битой" сессии
            TokenManager.logout()
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                apiService.register(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = withContext(Dispatchers.IO) { apiService.getUsers() }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = withContext(Dispatchers.IO) { apiService.getGroups() }
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        TokenManager.logout()
    }

    override fun isLoggedIn(): Boolean = TokenManager.isLoggedIn()

    override fun getUserId(): Long? = TokenManager.userId
}