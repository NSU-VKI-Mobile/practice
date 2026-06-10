package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @GET("/groups")
    suspend fun getGroups(): List<GroupDto>

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("users/login/{login}")
    suspend fun getUserByLogin(
        @Path("login") login: String
    ): UserDto
}