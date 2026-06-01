package ci.nsu.mobile.main.data.remote

import ci.nsu.mobile.main.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("users/login/{login}")
    suspend fun getUserByLogin(@Path("login") login: String): UserDto
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Unit // Сервер возвращает 200 OK без тела

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}