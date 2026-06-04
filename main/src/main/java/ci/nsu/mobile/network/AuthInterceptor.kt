package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.data.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: android.content.Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        val token = TokenManager.getToken(context)

        requestBuilder.addHeader("Content-Type", "application/json")

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}