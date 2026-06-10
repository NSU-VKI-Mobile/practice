// Task_6: Retrofit-сервис — описывает все API-эндпоинты.
package ci.nsu.moble.main.data.remote

import ci.nsu.moble.main.data.remote.dto.GroupDto
import ci.nsu.moble.main.data.remote.dto.LoginRequest
import ci.nsu.moble.main.data.remote.dto.RegisterRequest
import ci.nsu.moble.main.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}
