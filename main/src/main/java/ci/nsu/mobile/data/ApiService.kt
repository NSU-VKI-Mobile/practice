package ci.nsu.mobile.data

import ci.nsu.mobile.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Наш главный интерфейс для работы с сетью. Retrofit сам превратит эти методы в реальные запросы
interface ApiService {

    // Авторизация: шлём логин/пароль, в ответ получаем объект с JWT-токеном
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<UserDto>

    // Регистрация: отправляем большой пакет данных студента, в ответ получаем пустой Unit
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit>

    // Получение списка пользователей для отображения на главном экране (MainScreen)
    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    // Получение списка групп для нашей выпадашки на экране регистрации
    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}
