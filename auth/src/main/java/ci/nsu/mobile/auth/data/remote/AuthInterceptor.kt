package ci.nsu.mobile.auth.data.remote

import ci.nsu.mobile.auth.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val token = TokenManager.token

        requestBuilder.addHeader("Content-Type", "application/json")

        // Если токен есть в хранилище, добавляем его в заголовок
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}