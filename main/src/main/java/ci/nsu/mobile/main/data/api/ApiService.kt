package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.data.dto.LoginRequest
import ci.nsu.mobile.main.data.dto.LoginResponse
import ci.nsu.mobile.main.data.dto.RegisterRequest
import ci.nsu.mobile.main.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    )

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}