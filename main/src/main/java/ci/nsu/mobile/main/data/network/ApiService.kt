// ВАЖНО! Убедитесь, что строка package правильная!
package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.models.* // <-- Импортируем все DTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}