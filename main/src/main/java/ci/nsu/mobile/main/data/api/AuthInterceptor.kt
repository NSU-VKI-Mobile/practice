package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")

        tokenManager.getToken().let {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $it"
            )
        }

        return chain.proceed(requestBuilder.build())
    }
}