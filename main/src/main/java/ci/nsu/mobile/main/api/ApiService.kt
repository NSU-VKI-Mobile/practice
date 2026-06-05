package ci.nsu.mobile.main.api

import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.data.dto.LoginRequestDto
import ci.nsu.mobile.main.data.dto.LoginResponseDto
import ci.nsu.mobile.main.data.dto.RegisterRequestDto
import ci.nsu.mobile.main.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Works with HTTP requests
 */
interface ApiService {
    @POST("auth/login")//Retrofit сам создает HTTP запрос
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto//suspend позволяет выполнять запрос асинхронно

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto)

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>
}