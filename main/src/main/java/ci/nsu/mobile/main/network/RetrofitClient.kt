package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.data.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlin.jvm.java

object RetrofitClient {

    private const val BASE_URL =
        "http://192.168.200.160:8080/api/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

    val api: ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(ApiService::class.java)
}