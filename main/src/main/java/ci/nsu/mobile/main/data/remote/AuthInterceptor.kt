package ci.nsu.mobile.main.data.remote

import ci.nsu.mobile.main.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // 🟢 ЛОГИРУЕМ ЗАПРОС
        println("🔥 [INTERCEPTOR] Request URL: $url")

        val requestBuilder = originalRequest.newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")

        // 🟢 ПРОВЕРЯЕМ ТОКЕН
        val token = TokenManager.token
        println("🔥 [INTERCEPTOR] Token from manager: '${token?.take(10)}...'") // Показываем первые 10 символов

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            println("🔥 [INTERCEPTOR] Header 'Authorization' added.")
        } else {
            println("⚠️ [INTERCEPTOR] WARNING: Token is NULL or EMPTY! Server may return 401.")
        }

        return chain.proceed(requestBuilder.build())
    }
}