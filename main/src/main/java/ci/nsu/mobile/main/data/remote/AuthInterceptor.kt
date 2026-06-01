package ci.nsu.mobile.main.data.remote

import ci.nsu.mobile.main.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Добавляем заголовок Content-Type
        requestBuilder.addHeader("Content-Type", "application/json")

        // Если токен есть, добавляем Authorization header
        TokenManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}