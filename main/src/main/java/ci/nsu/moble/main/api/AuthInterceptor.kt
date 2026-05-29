package ci.nsu.moble.main.api

import okhttp3.Interceptor
import okhttp3.Response


/**
 *Adds user's token to every request
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")

        // Читаем актуальную строку токена через .token.value
        tokenManager.token.value?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // Выполняем сетевой запрос
        val response = chain.proceed(requestBuilder.build())

        // Если сервер ответил 401 Unauthorized — токен «протух» или невалиден
        if (response.code == 401) {
            // Стираем данные. Благодаря StateFlow наше UI-приложение
            // мгновенно узнает об этом и выбросит на экран логина!
            tokenManager.clear()
        }

        return response
    }
}