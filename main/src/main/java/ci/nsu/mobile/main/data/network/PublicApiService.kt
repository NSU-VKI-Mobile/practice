package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface PublicApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}