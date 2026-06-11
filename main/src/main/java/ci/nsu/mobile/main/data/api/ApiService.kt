import retrofit2.Response
import retrofit2.http.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

val json = Json { ignoreUnknownKeys = true }

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
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}