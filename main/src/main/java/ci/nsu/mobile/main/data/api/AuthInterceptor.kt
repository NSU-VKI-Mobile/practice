package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.token.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        tokenManager.getToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}