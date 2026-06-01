package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}