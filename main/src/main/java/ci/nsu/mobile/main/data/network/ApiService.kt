package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.network.dto.GroupDto
import ci.nsu.mobile.main.data.network.dto.LoginRequest
import ci.nsu.mobile.main.data.network.dto.LoginResponseDto
import ci.nsu.mobile.main.data.network.dto.RegisterRequest
import ci.nsu.mobile.main.data.network.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponseDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}
