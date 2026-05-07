package ci.nsu.mobile.main.units.data.api

import ci.nsu.mobile.main.units.data.model.GroupDto
import ci.nsu.mobile.main.units.data.model.LoginRequest
import ci.nsu.mobile.main.units.data.model.RegisterRequest
import ci.nsu.mobile.main.units.data.model.UserDto
import retrofit2.http.*
import retrofit2.Response

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}