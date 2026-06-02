package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

//перехватчик запросов (добавляет токен) - этот класс перехватывает каждый HTTP-запрос
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Получаем исходный запрос
        val originalRequest = chain.request()

        // 2. Создаем строитель для нового запроса
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json") //Добавляет заголовок
                                                                        // Content-Type: application/json
        // 3. Если есть токен - добавляем его в заголовок Authorization
        tokenManager.token?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
            //обавляет заголовок Authorization: Bearer <токен>, если пользователь авторизован
        }

        // 4. Выполняем запрос
        return chain.proceed(requestBuilder.build())
    }
}