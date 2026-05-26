package ci.nsu.moble.main.data.network

import ci.nsu.moble.main.data.models.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): retrofit2.Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): retrofit2.Response<AuthResponse>

    @GET("groups")
    suspend fun getGroups(): retrofit2.Response<List<GroupDto>>

    @GET("users")
    suspend fun getUsers(): retrofit2.Response<List<UserDto>>
}