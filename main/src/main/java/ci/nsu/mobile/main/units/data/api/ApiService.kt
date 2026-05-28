package ci.nsu.mobile.main.units.data.api

import ci.nsu.mobile.main.units.data.model.GroupDto
import ci.nsu.mobile.main.units.data.model.LoginRequest
import ci.nsu.mobile.main.units.data.model.RegisterRequest
import ci.nsu.mobile.main.units.data.model.UserDto
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}

@kotlinx.serialization.Serializable
data class LoginResponse(val token: String)