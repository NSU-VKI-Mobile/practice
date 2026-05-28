package ci.nsu.mobile.auth.data.network

import ci.nsu.mobile.auth.utils.UserPreferences
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.runBlocking

class AuthInterceptor(
    private val userPreferences: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        val isPublic = url.contains("/groups") ||
                url.contains("/auth/register") ||
                url.contains("/auth/login")

        if (!isPublic) {
            val token = runBlocking { userPreferences.getToken() }
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}