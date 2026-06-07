package ci.nsu.mobile.auth.data.network

import ci.nsu.mobile.domain.token.ITokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: ITokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val token = tokenManager.token
        requestBuilder.addHeader("Content-Type", "application/json")
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401 || response.code == 403) {
            kotlinx.coroutines.runBlocking {
                tokenManager.clear()
            }
        }
        return response
    }
}
