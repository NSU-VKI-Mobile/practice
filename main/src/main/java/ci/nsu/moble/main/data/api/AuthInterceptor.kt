package ci.nsu.moble.main.data.api

import android.util.Log
import ci.nsu.moble.main.data.storage.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
            .header("Content-Type", "application/json")

        val token = tokenManager.token
        if (token != null) {
            Log.d(TAG, "Adding token: Bearer $token")
            builder.header("Authorization", "Bearer $token")
        } else {
            Log.e(TAG, "Token is null! Request may fail.")
        }

        return chain.proceed(builder.build())
    }
}