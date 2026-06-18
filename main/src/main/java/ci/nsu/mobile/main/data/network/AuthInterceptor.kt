package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Добавляем Content-Type
        requestBuilder.addHeader("Content-Type", "application/json")

        // Добавляем токен, если он есть
        TokenManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}