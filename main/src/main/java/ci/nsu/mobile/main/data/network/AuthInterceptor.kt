// ВАЖНО! Убедитесь, что строка package правильная!
package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.TokenManager // <-- Studio предложит импортировать это
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        TokenManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}