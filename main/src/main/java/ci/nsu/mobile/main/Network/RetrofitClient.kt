package ci.nsu.mobile.main.Network

import ci.nsu.mobile.main.Auth.AuthInterceptor
import ci.nsu.mobile.main.Auth.TokenManager
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.200.160:8080/api/"

    val instance: ApiService by lazy {
        val contentType = "application/json".toMediaType()
        val client = OkHttpClient.Builder()
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(ApiService::class.java)
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun getApiService(tokenManager: TokenManager): ApiService {
        val authInterceptor = AuthInterceptor(tokenManager)
        val okHttpClient = client.newBuilder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}