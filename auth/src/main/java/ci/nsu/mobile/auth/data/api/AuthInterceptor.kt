package ci.nsu.mobile.auth.data.api

import ci.nsu.mobile.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder().apply {
            TokenManager.token?.let { addHeader("Authorization", "Bearer $it") }
        }.build()
        return chain.proceed(req)
    }
}