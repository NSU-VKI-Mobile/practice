package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.dto.LoginRequestDto
import ci.nsu.mobile.main.data.dto.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>
}