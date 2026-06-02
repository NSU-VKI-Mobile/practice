package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.models.ErrorResponse
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.network.ApiService
import ci.nsu.mobile.main.utils.TokenManager
import com.google.gson.Gson
import ci.nsu.mobile.main.utils.ErrorHandler

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun login(login: String, password: String): Result<String> {
        return try {
            val request = mapOf("login" to login, "password" to password)
            val response = apiService.login(request)
            tokenManager.token = response.token
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            // Логируем, что отправляем на сервер
            val gson = Gson()
            val jsonRequest = gson.toJson(registerRequest)
            println("=== ОТПРАВЛЯЕМ НА СЕРВЕР ===")
            println(jsonRequest)

            apiService.register(registerRequest)
            Result.success(Unit)
        } catch (e: Exception) {
            val userFriendlyMessage = ErrorHandler.getReadableErrorMessage(e)
            println("=== ОШИБКА ===")
            println(userFriendlyMessage)
            Result.failure(Exception(userFriendlyMessage))
        }
    }

    /**
     * Парсит тело ошибки и возвращает понятное сообщение
     */
    private fun parseErrorMessage(errorBody: String?, httpCode: Int): String {
        // Если нет тела ошибки - возвращаем сообщение по коду HTTP
        if (errorBody.isNullOrEmpty()) {
            return getDefaultErrorMessage(httpCode)
        }

        return try {
            // Пробуем распарсить как JSON
            val gson = Gson()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            // Ищем сообщение в разных полях
            val message = errorResponse.message
                ?: errorResponse.error
                ?: errorBody
                ?: getDefaultErrorMessage(httpCode)

            // Добавляем код ошибки для ясности
            when (httpCode) {
                400 -> "Ошибка в данных: $message"
                409 -> "Конфликт: $message"
                422 -> "Ошибка валидации: $message"
                else -> "$message (код: $httpCode)"
            }
        } catch (e: Exception) {
            // Если не удалось распарсить JSON - выводим "сырое" тело
            println("Ошибка парсинга JSON: ${e.message}")
            "Ошибка сервера: $errorBody"
        }
    }

    /**
     * Стандартные сообщения по кодам HTTP
     */
    private fun getDefaultErrorMessage(httpCode: Int): String {
        return when (httpCode) {
            400 -> "Неверные данные. Проверьте все поля"
            401 -> "Не авторизован"
            403 -> "Доступ запрещен"
            404 -> "Сервис не найден"
            409 -> "Пользователь с таким логином или email уже существует"
            422 -> "Ошибка валидации. Проверьте формат данных"
            500 -> "Внутренняя ошибка сервера. Попробуйте позже"
            502 -> "Сервер временно недоступен"
            503 -> "Сервис недоступен. Попробуйте позже"
            else -> "Ошибка сервера: $httpCode"
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clear()
    }
}