package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.network.NetworkClient
import ci.nsu.mobile.main.data.security.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class AuthRepository {
    private val apiService = NetworkClient.apiService
    private val tokenManager = TokenManager

    // 🔧 ПЕРЕКЛЮЧАТЕЛЬ: true - моки (для дома), false - реальный сервер (для колледжа)
    private val USE_MOCK_DATA = true

    suspend fun login(login: String, password: String): Result<UserDto> = withContext(Dispatchers.IO) {
        if (USE_MOCK_DATA) {
            delay(1000) // Имитация задержки сети
            if (login.isNotBlank() && password.isNotBlank()) {
                // ✅ Принимаем ЛЮБЫЕ непустые данные для тестирования UI
                val mockUser = UserDto(
                    id = 1,
                    login = login, // Используем введённый логин
                    email = "user@test.com",
                    phoneNumber = "+79001234567",
                    token = "mock_jwt_token_12345",
                    person = PersonDto(
                        firstName = "Test",
                        lastName = "User",
                        middleName = null,
                        birthDate = "2000-01-01",
                        gender = "MALE",
                        groupId = 1
                    )
                )
                tokenManager.token = mockUser.token // Сохраняем токен
                Result.success(mockUser)
            } else {
                Result.failure(IOException("Введите логин и пароль"))
            }
        } else {
            // Реальный запрос к серверу
            try {
                val response = apiService.login(LoginRequest(login, password))
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    user.token?.let { tokenManager.token = it }
                    Result.success(user)
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Неверный логин или пароль"
                        400 -> "Ошибка валидации данных"
                        500 -> "Ошибка сервера"
                        else -> "Ошибка входа: ${response.code()}"
                    }
                    Result.failure(IOException(errorMsg))
                }
            } catch (e: java.net.SocketTimeoutException) {
                Result.failure(IOException("Сервер не отвечает. Проверьте подключение."))
            } catch (e: java.net.UnknownHostException) {
                Result.failure(IOException("Сервер недоступен."))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> = withContext(Dispatchers.IO) {
        if (USE_MOCK_DATA) {
            delay(500)
            val mockGroups = listOf(
                GroupDto(id = 1, name = "ПИ-123"),
                GroupDto(id = 2, name = "ИБ-456"),
                GroupDto(id = 3, name = "ИС-789")
            )
            Result.success(mockGroups)
        } else {
            // Реальный запрос...
            try {
                val response = apiService.getGroups()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(IOException("Ошибка загрузки групп"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun register(request: RegisterRequest): Result<Unit> = withContext(Dispatchers.IO) {
        if (USE_MOCK_DATA) {
            delay(1000)
            // ✅ Всегда успешная регистрация для тестирования
            // Базовая валидация (как сделал бы реальный сервер)
            return@withContext when {
                request.login.length < 4 ->
                    Result.failure(IOException("Логин должен содержать минимум 4 символа"))
                request.password.length < 6 ->
                    Result.failure(IOException("Пароль должен содержать минимум 6 символов"))
                !android.util.Patterns.EMAIL_ADDRESS.matcher(request.email).matches() ->
                    Result.failure(IOException("Некорректный формат email"))
                !request.phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$")) ->
                    Result.failure(IOException("Некорректный формат телефона"))
                request.person.firstName.isBlank() || request.person.lastName.isBlank() ->
                    Result.failure(IOException("Имя и фамилия обязательны"))
                else -> Result.success(Unit) // Всё ок
            }
        } else {
            // Реальная реализация...
            try {
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(IOException("Ошибка регистрации"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        if (USE_MOCK_DATA) {
            delay(500)
            val mockUsers = listOf(
                UserDto(
                    id = 1,
                    login = "user1",
                    email = "user1@test.com",
                    phoneNumber = "+79991112233",
                    token = null,
                    person = PersonDto(
                        firstName = "Иван",
                        lastName = "Иванов",
                        middleName = "Иванович",
                        birthDate = "2000-01-01",
                        gender = "MALE",
                        groupId = 1
                    )
                ),
                UserDto(
                    id = 2,
                    login = "user2",
                    email = "user2@test.com",
                    phoneNumber = "+79992223344",
                    token = null,
                    person = PersonDto(
                        firstName = "Петр",
                        lastName = "Петров",
                        middleName = "Петрович",
                        birthDate = "2001-05-15",
                        gender = "MALE",
                        groupId = 2
                    )
                )
            )
            Result.success(mockUsers)
        } else {
            // Реальный запрос...
            try {
                val response = apiService.getUsers()
                if (response.code() == 401) {
                    tokenManager.clearToken()
                    return@withContext Result.failure(IOException("Сессия истекла"))
                }
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(IOException("Ошибка загрузки пользователей"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}