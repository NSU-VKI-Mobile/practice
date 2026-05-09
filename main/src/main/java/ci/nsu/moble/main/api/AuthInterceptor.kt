package ci.nsu.moble.main.api

import okhttp3.Interceptor
import okhttp3.Response


/**
 *Adds user's token to every request
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder() // create new request
        requestBuilder.addHeader("Content-Type", "application/json") // declaring that we're using json
        // do we have token? (let only executes when not null)
        tokenManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it") // Bearer is a code for jwt-token
        }
        return chain.proceed(requestBuilder.build()) // send out new request
    }
}