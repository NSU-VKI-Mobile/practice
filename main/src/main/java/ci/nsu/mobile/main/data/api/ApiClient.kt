package ci.nsu.mobile.main.data.api
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiClient {

    private const val BASE_URL = "http://192.168.200.160:8080/api/"

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(AuthInterceptor())
        .build()


    val api: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(ApiService::class.java)
}