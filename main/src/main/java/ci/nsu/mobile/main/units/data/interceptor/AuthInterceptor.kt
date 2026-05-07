package ci.nsu.mobile.main.units.data.interceptor

import ci.nsu.mobile.main.units.data.token.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
            .addHeader("Content-Type", "application/json")
        tokenManager.token?.let { token ->
            builder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(builder.build())
    }
}