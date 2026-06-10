package ci.nsu.mobile.data

import ci.nsu.mobile.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Наш репозиторий — прослойка между сетью и ViewModel. Он пинает ApiService в фоновом потоке
class AuthRepository {

    // Получаем наш готовый API-сервис
    private val apiService = RetrofitClient.getApiService()

    // Вход в систему
    suspend fun login(loginRequest: LoginRequest): Result<UserDto> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                val user = response.body()
                if (user?.token != null) {
                    TokenManager.token = user.token
                    Result.success(user)
                } else {
                    Result.failure(Exception("Токен не получен"))
                }
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Регистрация нового студента
    suspend fun register(registerRequest: RegisterRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка регистрации: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Скачиваем список групп для нашей красивой выпадашки на экране рега
    suspend fun getGroups(): Result<List<GroupDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getGroups()
            if (response.isSuccessful) {
                android.util.Log.d("API_TEST", "Группы успешно получены: ${response.body()?.size}")
                Result.success(response.body() ?: emptyList())
            } else {
                android.util.Log.e("API_TEST", "Ошибка сервера: ${response.code()} ${response.errorBody()?.string()}")
                Result.failure(Exception("Код ошибки: ${response.code()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("API_TEST", "Критическая ошибка сети!", e)
            Result.failure(e)
        }
    }

    // Получаем список юзеров для отображения на главном экране (MainScreen)
    suspend fun getUsers(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Ошибка доступа к списку пользователей"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
