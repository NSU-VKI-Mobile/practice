package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.toUserDto
import ci.nsu.mobile.main.data.model.UserResponse
import ci.nsu.mobile.main.data.model.toPersonDto
import ci.nsu.mobile.main.data.network.NetworkClient
import ci.nsu.mobile.main.data.security.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class AuthRepository {
    private val apiService = NetworkClient.apiService
    private val tokenManager = TokenManager

    //  ПЕРЕКЛЮЧАТЕЛЬ: true - моки (для дома), false - реальный сервер (для колледжа)
    private val USE_MOCK_DATA = false

    private suspend fun mapToUserDto(response: UserResponse, token: String? = null): UserDto {
        var personDto: PersonDto? = null

        response.personId?.let { pid ->
            runCatching {
                val personResp = apiService.getPersonById(pid)
                if (personResp.isSuccessful) personResp.body() else null
            }.getOrNull()?.toPersonDto()?.also { personDto = it }
        }

        return response.toUserDto(personDto, token)
    }

    suspend fun login(login: String, password: String): Result<UserDto> = withContext(Dispatchers.IO) {
        if (USE_MOCK_DATA) {
            delay(1000) // Имитация задержки сети
            if (login.isNotBlank() && password.isNotBlank()) {
                // Принимаем ЛЮБЫЕ непустые данные для тестирования UI
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
                val loginResp = apiService.login(LoginRequest(login, password))
                if (loginResp.isSuccessful && loginResp.body() != null) {
                    tokenManager.token = loginResp.body()!!.token

                    val usersResp = apiService.getUsers()
                    if (usersResp.isSuccessful && usersResp.body() != null) {
                        val targetUser = usersResp.body()!!.find { it.login == login }
                        if (targetUser != null) {
                            Result.success(mapToUserDto(targetUser, tokenManager.token))
                        } else {
                            Result.failure(IOException("Профиль не найден"))
                        }
                    } else {
                        Result.failure(IOException("Ошибка получения списка пользователей"))
                    }
                } else {
                    Result.failure(IOException("Неверный логин или пароль"))
                }
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
                    val errorMsg = "${response.code()}"
                    Result.failure(IOException(errorMsg))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        if (USE_MOCK_DATA) {
            // --- МОК-РЕАЛИЗАЦИЯ (для дома/тестов) ---
            delay(500)
            val mockUsers = listOf(
                UserDto(
                    id = 1,
                    login = "user1",
                    email = "user1@test.com",
                    phoneNumber = "+79991112233",
                    person = PersonDto(
                        firstName = "Иван",
                        lastName = "Иванов",
                        middleName = "Иванович",
                        birthDate = "2000-01-01",
                        gender = "MALE",
                        groupId = 1
                    ),
                    token = null
                ),
                UserDto(
                    id = 2,
                    login = "user2",
                    email = "user2@test.com",
                    phoneNumber = "+79992223344",
                    person = PersonDto(
                        firstName = "Петр",
                        lastName = "Петров",
                        middleName = "Петрович",
                        birthDate = "2001-05-15",
                        gender = "MALE",
                        groupId = 2
                    ),
                    token = null
                )
            )
            Result.success(mockUsers)

        } else {
            // --- РЕАЛЬНАЯ РЕАЛИЗАЦИЯ (для колледжа) ---
            try {
                // 1. Запрашиваем "грязные" данные с сервера
                val response = apiService.getUsers()

                if (response.isSuccessful && response.body() != null) {
                    val rawUsers = response.body()!! // Это List<UserResponse>

                    // 2. Конвертируем каждый элемент в чистый UserDto
                    // Используем наш хелпер mapToUserDto, который внутри подтянет персону
                    val cleanUsers = rawUsers.map { userResponse ->
                        mapToUserDto(userResponse)
                    }

                    Result.success(cleanUsers)

                } else {
                    // Обработка HTTP ошибок (401, 500 и т.д.)
                    if (response.code() == 401) {
                        tokenManager.clearToken()
                        return@withContext Result.failure(IOException("Сессия истекла"))
                    }
                    Result.failure(IOException("Ошибка загрузки пользователей: ${response.code()}"))
                }

            } catch (e: Exception) {
                // Обработка сетевых ошибок (таймаут, нет интернета)
                Result.failure(e)
            }
        }
    }
}