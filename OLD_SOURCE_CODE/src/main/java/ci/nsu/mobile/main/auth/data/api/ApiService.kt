package ci.nsu.mobile.main.auth.data.api

import ci.nsu.mobile.main.auth.data.dto.LoginRequest
import ci.nsu.mobile.main.auth.data.dto.AuthResponse
import ci.nsu.mobile.main.auth.data.dto.GroupDto
import ci.nsu.mobile.main.auth.data.dto.RegisterRequest
import ci.nsu.mobile.main.auth.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @GET("users/login/{login}")
    suspend fun getUserByLogin(@Path("login") login: String): Response<UserDto>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}