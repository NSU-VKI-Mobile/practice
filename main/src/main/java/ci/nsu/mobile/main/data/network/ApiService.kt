package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.models.*
import retrofit2.http.*

interface ApiService { //объявление интерфейса
    //метод входа в систему
    @POST("auth/login") //Отправляет POST-запрос по адресу auth/login
    suspend fun login( //Работает асинхронно (suspend): Функция может "засыпать" и ждать ответа от сервера.
                        // Не блокирует главный поток (интерфейс не зависает)
        @Body request: Map<String, String> //Retrofit превратит Map с логином и паролем в JSON
    ): AuthResponse //Возвращает AuthResponse (объект с токеном) - Retrofit сам создаст его из JSON.

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Unit

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}

// Retrofit сам создает объект, который умеет:
// 1. Отправлять GET-запрос на http://.../api/users
// 2. Получать JSON
// 3. Превращать JSON в List<UserDto>
// 4. Обрабатывать ошибки