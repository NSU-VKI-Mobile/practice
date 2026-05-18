package ci.nsu.mobile.main.Network;

import RegisterRequest
import retrofit2.http.*
import ci.nsu.mobile.main.Data.Models.*

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

// Simple data class for login
data class LoginRequest(
        val login: String,
        val password: String
)