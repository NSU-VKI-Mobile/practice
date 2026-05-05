package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.storage.TokenManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit

object NetworkModule {

    private const val BASE_URL = "http://192.168.200.160:8080/api/"
    internal val json = Json { ignoreUnknownKeys = true }

    private val contentTypeInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .removeHeader("Content-Type")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    fun providePublicApiService(): PublicApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(contentTypeInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PublicApiService::class.java)
    }

    fun provideApiService(): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}