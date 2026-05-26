package ci.nsu.mobile.main.data.remote

import ci.nsu.mobile.main.data.model.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Unit

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/login/{login}")
    suspend fun getUserByLogin(@Path("login") login: String): User
}