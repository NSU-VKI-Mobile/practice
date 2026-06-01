package ci.nsu.mobile.main.network

import ci.nsu.mobile.main.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val request =
            chain.request()
                .newBuilder()
                .addHeader(
                    "Content-Type",
                    "application/json"
                )

        TokenManager.token?.let {
            request.addHeader(
                "Authorization",
                "Bearer $it"
            )
        }

        return chain.proceed(request.build())
    }
}