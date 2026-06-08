package ci.nsu.mobile.main.auth.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegistrationRequest
import ci.nsu.mobile.main.data.model.AuthResponse


interface AuthApiService {

    @POST("/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("/auth/register")
    suspend fun register(
        @Body request: RegistrationRequest
    ): AuthResponse

    @GET("/groups")
    suspend fun getGroups(): List<GroupDto>
}