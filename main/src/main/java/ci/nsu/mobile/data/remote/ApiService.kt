package ci.nsu.mobile.data.remote

import ci.nsu.mobile.data.model.*
import ci.nsu.mobile.data.repository.AuthRepository
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: Map<String, String>): Response<AuthRepository.AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthRepository.AuthResponse>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>

}