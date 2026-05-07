package ci.nsu.mobile.main.api

import ci.nsu.mobile.main.api.requestData.AuthTokenRespone
import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.api.requestData.LoginRequest
import ci.nsu.mobile.main.api.requestData.RegisterRequest
import ci.nsu.mobile.main.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService{
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthTokenRespone>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}