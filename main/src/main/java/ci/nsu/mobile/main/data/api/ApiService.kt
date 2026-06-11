package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.model.*
import retrofit2.Response
import retrofit2.http.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit


interface ApiService {
    @OptIn(InternalSerializationApi::class)
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @OptIn(InternalSerializationApi::class)
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @OptIn(InternalSerializationApi::class)
    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @OptIn(InternalSerializationApi::class)
    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}

val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.200.160:8080/api/")
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

object Api {
    val service: ApiService = retrofit.create(ApiService::class.java)
}