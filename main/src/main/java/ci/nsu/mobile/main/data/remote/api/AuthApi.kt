package ci.nsu.mobile.main.data.remote.api

import ci.nsu.mobile.main.data.remote.model.GroupDto
import ci.nsu.mobile.main.data.remote.model.LoginResponse
import ci.nsu.mobile.main.data.remote.model.RegisterRequest
import ci.nsu.mobile.main.data.remote.model.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}