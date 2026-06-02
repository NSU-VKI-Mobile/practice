package ci.nsu.moble.main.data

import ci.nsu.moble.main.data.dto.AuthResponseDto
import ci.nsu.moble.main.data.dto.GroupDto
import ci.nsu.moble.main.data.dto.RegisterRequest
import ci.nsu.moble.main.data.dto.UserDto
import ci.nsu.moble.main.data.dto.UserLoginRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: UserLoginRequestDto
    ): Response<AuthResponseDto>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}