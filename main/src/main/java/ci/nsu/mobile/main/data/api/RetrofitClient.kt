package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.datasource.local.TokenManager
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit 
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // For emulator to reach computer's localhost
    const val BASE_URL = "http://10.0.2.2:8080/" // "http://10.0.2.2:8080/"  "http://192.168.200.160:8080/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private fun getClient(tokenManager: TokenManager): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = AuthInterceptor(tokenManager)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun getApiService(tokenManager: TokenManager): ApiService {
        val client = getClient(tokenManager)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(KotlinxSerializationConverterFactory(json))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
