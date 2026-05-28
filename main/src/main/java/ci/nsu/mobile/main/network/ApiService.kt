package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.LoginRequest
import ci.nsu.mobile.main.data.models.LoginResponse
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>
}