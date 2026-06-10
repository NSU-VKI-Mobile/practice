package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val requestBuilder =
            chain.request()
                .newBuilder()

        requestBuilder.addHeader(
            "Content-Type",
            "application/json"
        )

        TokenManager.token?.let {

            requestBuilder.addHeader(
                "Authorization",
                "Bearer $it"
            )
        }

        return chain.proceed(
            requestBuilder.build()
        )
    }
}