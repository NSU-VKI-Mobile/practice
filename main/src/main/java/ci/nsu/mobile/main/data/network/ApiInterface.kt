package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}