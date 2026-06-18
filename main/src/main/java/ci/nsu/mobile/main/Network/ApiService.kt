package ci.nsu.mobile.main.Network

import ci.nsu.mobile.main.Data.Models.AuthResponse
import ci.nsu.mobile.main.Data.Models.GroupDto
import ci.nsu.mobile.main.Data.Models.LoginRequest
import ci.nsu.mobile.main.Data.Models.RegisterRequest
import ci.nsu.mobile.main.Data.Models.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}