package ci.nsu.mobile.main.data

import ci.nsu.moble.main.data.dto.UserDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        val token = TokenManager.token

        requestBuilder.addHeader("Content-Type", "application/json")
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>

    @GET("users")
    suspend fun getUsers(): List<UserDto>
}

object RetrofitClient {
    private const val BASE_URL = "http://192.168.200.160:8080/api/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}

class AuthRepository {
    suspend fun login(login: String, pass: String): Result<AuthResponse> = runCatching {
        RetrofitClient.api.login(LoginRequest(login, pass))
    }

    suspend fun register(req: RegisterRequest): Result<Unit> = runCatching {
        RetrofitClient.api.register(req)
    }

    suspend fun getUsers(): Result<List<UserDto>> = runCatching {
        RetrofitClient.api.getUsers()
    }

    suspend fun getGroups(): Result<List<GroupDto>> = runCatching {
        RetrofitClient.api.getGroups()
    }
}