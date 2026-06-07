package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.network.model.AuthResponse
import ci.nsu.mobile.main.data.network.model.GroupDto
import ci.nsu.mobile.main.data.network.model.LoginRequest
import ci.nsu.mobile.main.data.network.model.RegisterRequest
import ci.nsu.mobile.main.data.network.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/groups")
    suspend fun getGroups(): Response<List<GroupDto>>
    @POST("/auth/register")
    suspend fun registerUser(@Body body: RegisterRequest): Response<AuthResponse>
    @POST("/auth/login")
    suspend fun loginUser(@Body body: LoginRequest): Response<AuthResponse>
    @GET("/users")
    suspend fun getUsers(): Response<List<User>>
}