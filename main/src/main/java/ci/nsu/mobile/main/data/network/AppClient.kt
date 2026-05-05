package ci.nsu.mobile.main.data.network

import android.os.Build
import ci.nsu.mobile.main.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
object AppClient {
    private val baseUrl: String = BuildConfig.API_BASE_URL

    // private val baseUrl: String = "http://192.168.200.160:8080/api/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val apiService: AppService = retrofit.create(AppService::class.java)
}

private fun isProbablyEmulator(): Boolean {
    return Build.FINGERPRINT.contains("generic", ignoreCase = true) ||
        Build.MODEL.contains("Emulator", ignoreCase = true) ||
        Build.MODEL.contains("Android SDK built for", ignoreCase = true) ||
        Build.MANUFACTURER.contains("Genymotion", ignoreCase = true) ||
        Build.PRODUCT.contains("sdk", ignoreCase = true) ||
        Build.HARDWARE.contains("ranchu", ignoreCase = true)
}
