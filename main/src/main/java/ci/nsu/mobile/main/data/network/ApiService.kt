package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.LoginResponse
import ci.nsu.mobile.main.data.model.PersonResponse
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>

    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("persons/{personId}")
    suspend fun getPersonById(@Path("personId") personId: Int): Response<PersonResponse>
}