package ci.nsu.mobile.data.remote

import android.util.Log
import ci.nsu.mobile.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = TokenManager.token

        Log.d("API", "INTERCEPTOR TOKEN = $token")

        val request = chain.request().newBuilder()

        request.addHeader("Content-Type", "application/json")

        if (!token.isNullOrEmpty()) {
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}