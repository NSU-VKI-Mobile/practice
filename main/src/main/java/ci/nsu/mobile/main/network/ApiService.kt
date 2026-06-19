package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.data.AuthResponse
import ci.nsu.mobile.main.data.GroupDto
import ci.nsu.mobile.main.data.LoginRequest
import ci.nsu.mobile.main.data.RegisterRequest
import ci.nsu.mobile.main.data.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Unit

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}