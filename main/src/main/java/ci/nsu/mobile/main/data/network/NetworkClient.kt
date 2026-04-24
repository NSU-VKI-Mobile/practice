package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.security.AuthInterceptor
import ci.nsu.mobile.main.data.security.TokenManager
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    // Ленивая инициализация: создастся только при первом обращении
    val apiService: ApiService by lazy {

        // 1. Логгер для отладки (показывает запросы и ответы в Logcat)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2. Клиент с нашим перехватчиком авторизации
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        // 3. Настройка сериализации
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        // 4. Сборка Retrofit
        Retrofit.Builder()
            .baseUrl("http://192.168.200.160:8080/api/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}