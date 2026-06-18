package ci.nsu.mobile.main.data.security

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private val AUTH_ENDPOINTS = listOf(
        "/auth/login",
        "/auth/register",
        "/auth/refresh" // если будет
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (AUTH_ENDPOINTS.any { originalRequest.url.encodedPath.contains(it) }) {
            return chain.proceed(originalRequest)
        }

        val requestBuilder = originalRequest.newBuilder().addHeader("Content-Type", "application/json")

        // Чтение токена синхронно. Это безопасно: SharedPreferences кэширует данные в памяти,
        // а операция чтения занимает микросекунды и не блокирует сетевой пул OkHttp.
        val token = TokenManager.token

        requestBuilder.addHeader("Content-Type", "application/json")

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}