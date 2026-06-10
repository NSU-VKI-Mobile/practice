package ci.nsu.mobile.data

import okhttp3.Interceptor
import okhttp3.Response

// Наш сетевой перехватчик. Он ловит каждый исходящий запрос к серверу и дописывает в него нужные данные
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Вытаскиваем сохраненный JWT-токен (теперь красиво и без контекста, строго по ТЗ)
        val token = TokenManager.token

        // Добавляем стандартные заголовки, чтобы сервер понимал, что мы общаемся через JSON
        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("Accept", "application/json")

        // Если токен есть (мы уже залогинены), пихаем его в заголовок Authorization
        // Без этого Bearer-токена сервер вернет ошибку 401 Unauthorized на закрытые эндпоинты
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // Отправляем модифицированный запрос дальше по цепочке в сеть
        return chain.proceed(requestBuilder.build())
    }
}
