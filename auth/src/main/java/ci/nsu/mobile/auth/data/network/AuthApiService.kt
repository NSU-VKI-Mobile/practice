package ci.nsu.mobile.auth.data.network

import ci.nsu.mobile.auth.data.dto.AuthResponse
import ci.nsu.mobile.auth.data.dto.GroupDto
import ci.nsu.mobile.auth.data.dto.LoginRequest
import ci.nsu.mobile.auth.data.dto.RegisterRequest
import ci.nsu.mobile.auth.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>

    @GET("users")
    suspend fun getUsers(): List<UserDto>
}