package ci.nsu.mobile.auth.data.remote.api

import ci.nsu.mobile.auth.data.model.GroupDto
import ci.nsu.mobile.auth.data.model.LoginResponse
import ci.nsu.mobile.auth.data.model.RegisterRequest
import ci.nsu.mobile.auth.data.model.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}