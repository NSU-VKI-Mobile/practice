package ci.nsu.mobile.auth.network

import ci.nsu.mobile.auth.data.models.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Map<String, String>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}