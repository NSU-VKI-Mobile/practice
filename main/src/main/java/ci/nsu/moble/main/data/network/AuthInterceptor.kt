package ci.nsu.moble.main.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val token: String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            println(" AuthInterceptor: токен добавлен")
        } else {
            println(" AuthInterceptor: токен отсутствует")
        }

        return chain.proceed(requestBuilder.build())
    }
}