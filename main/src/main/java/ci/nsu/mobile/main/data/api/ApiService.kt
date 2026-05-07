package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.model.dto.*
import ci.nsu.mobile.main.data.model.request.*
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