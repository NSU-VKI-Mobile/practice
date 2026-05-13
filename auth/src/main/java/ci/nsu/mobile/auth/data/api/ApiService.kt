package ci.nsu.mobile.auth.data.api

import ci.nsu.mobile.auth.data.model.dto.GroupDto
import ci.nsu.mobile.auth.data.model.dto.UserDto
import ci.nsu.mobile.auth.data.model.request.LoginRequest
import ci.nsu.mobile.auth.data.model.request.RegisterRequest
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