package ci.nsu.mobile.auth.data.network

import retrofit2.Response
import ci.nsu.mobile.auth.data.models.*
import retrofit2.http.*

interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>
}