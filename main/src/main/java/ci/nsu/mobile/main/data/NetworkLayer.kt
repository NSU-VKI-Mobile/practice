package ci.nsu.mobile.main.data

import ci.nsu.mobile.main.MainActivity
import ci.nsu.mobile.main.R
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")
        TokenManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}
object RetrofitClient {

    //private val context = .instance
    //val xmlResource = resources.getXml(R.xml.network_security_config)
    private const val BASE_URL = "http://192.168.200.160:8080/api/"
    private val client = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
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
    suspend fun login(login: String, pass: String): Result<AuthResponse> = runCatching { RetrofitClient.api.login(LoginRequest(login, pass)) }
    suspend fun register(req: RegisterRequest): Result<Unit> = runCatching { RetrofitClient.api.register(req) }
    suspend fun getUsers(): Result<List<UserDto>> = runCatching { RetrofitClient.api.getUsers() }
    suspend fun getGroups(): Result<List<GroupDto>> = runCatching { RetrofitClient.api.getGroups() }
}