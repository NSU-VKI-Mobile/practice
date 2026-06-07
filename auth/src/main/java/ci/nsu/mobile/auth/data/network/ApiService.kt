package ci.nsu.mobile.auth.data.network


import ci.nsu.mobile.auth.data.network.models.AuthResponse
import ci.nsu.mobile.auth.data.network.models.GroupDto
import ci.nsu.mobile.auth.data.network.models.LoginRequest
import ci.nsu.mobile.auth.data.network.models.RegisterRequest
import ci.nsu.mobile.domain.models.User
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