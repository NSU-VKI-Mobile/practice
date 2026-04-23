package ci.nsu.mobile.main.data.security

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

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