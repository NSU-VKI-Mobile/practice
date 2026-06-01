package com.example.auth.data.network


import com.example.auth.data.network.model.AuthResponse
import com.example.auth.data.network.model.GroupDto
import com.example.auth.data.network.model.LoginRequest
import com.example.auth.data.network.model.RegisterRequest
import com.example.auth.data.network.model.UserDto
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
    suspend fun getUsers(): Response<List<UserDto>>
}