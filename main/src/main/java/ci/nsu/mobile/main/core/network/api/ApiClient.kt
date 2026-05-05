package ci.nsu.mobile.main.core.network.api

import android.util.Log
import ci.nsu.mobile.main.model.dto.AuthResponse
import ci.nsu.mobile.main.model.dto.GroupDto
import ci.nsu.mobile.main.model.dto.LoginRequest
import ci.nsu.mobile.main.model.dto.RegisterRequest
import ci.nsu.mobile.main.model.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiClient {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("/groups")
    suspend fun getGroups(): List<GroupDto>

    @GET("/users")
    suspend fun getUsers(): List<UserDto>
}