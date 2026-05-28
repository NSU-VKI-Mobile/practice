package com.example.auth.api


import com.example.auth.api.requestData.AuthTokenRespone
import com.example.auth.api.requestData.LoginRequest
import com.example.auth.api.requestData.RegisterRequest
import com.example.auth.data.dto.GroupDto
import com.example.auth.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService{
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthTokenRespone>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}