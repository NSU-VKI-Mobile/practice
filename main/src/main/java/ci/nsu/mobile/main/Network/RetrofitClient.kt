package ci.nsu.mobile.main.Network

import ci.nsu.mobile.main.Auth.AuthInterceptor
import ci.nsu.mobile.main.Auth.TokenManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.200.160:8080/api/"

    fun getApiService(tokenManager: TokenManager): ApiService {
        val contentType = "application/json".toMediaType()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(logging)
            .build()

        val json = Json {
            ignoreUnknownKeys = true          // игнорировать неизвестные поля в JSON
            isLenient = true                  // допускать нестрогий синтаксис (например, комментарии)
            coerceInputValues = true          // для работы с nullable типами
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}