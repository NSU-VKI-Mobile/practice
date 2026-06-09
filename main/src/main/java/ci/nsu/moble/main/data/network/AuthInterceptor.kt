package ci.nsu.moble.main.auth.data.network

import ci.nsu.moble.main.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.runBlocking

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.getToken() }
        println("🔑 AuthInterceptor: токен = ${token?.take(50)}")

        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")

        if (!token.isNullOrBlank()) {
            newRequest.addHeader("Authorization", "Bearer $token")
            println("✅ Заголовок Authorization добавлен")
        } else {
            println("❌ Токен пустой")
        }

        return chain.proceed(newRequest.build())
    }
}