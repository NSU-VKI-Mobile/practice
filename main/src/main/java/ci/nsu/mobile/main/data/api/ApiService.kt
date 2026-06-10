package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.LoginRequest
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.TokenResponse
import ci.nsu.mobile.main.data.models.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): TokenResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Unit

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>

    @GET("users/login/{login}")
    suspend fun getUserByLogin(@Path("login") login: String): UserDto
}