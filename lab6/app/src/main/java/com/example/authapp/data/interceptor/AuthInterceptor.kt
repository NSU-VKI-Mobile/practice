package com.example.authapp.data.interceptor

import com.example.authapp.data.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

// AuthInterceptor — перехватчик HTTP-запросов
//
// Как это работает:
// 1. Приложение делает запрос к серверу (например GET /users)
// 2. OkHttp перед отправкой пропускает запрос через AuthInterceptor
// 3. Interceptor добавляет заголовок Authorization: Bearer <токен>
// 4. Только после этого запрос уходит на сервер
//
// Аналогия: секретарь (Interceptor) ставит печать (токен) на каждое письмо
// перед тем как отправить его из офиса

class AuthInterceptor : Interceptor {

    // intercept — вызывается при КАЖДОМ HTTP-запросе
    override fun intercept(chain: Interceptor.Chain): Response {
        // chain.request() — оригинальный запрос без наших добавок
        val originalRequest = chain.request()

        // Создаём новый запрос на основе оригинального
        val requestBuilder = originalRequest.newBuilder()

        // Добавляем заголовок Content-Type (формат данных — JSON)
        requestBuilder.addHeader("Content-Type", "application/json")

        // Если есть токен — добавляем его в заголовок Authorization
        val token = TokenManager.token
        token?.let {
            // Bearer — стандартный формат: "Bearer eyJhbGciOiJIUzI1..."
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        // Отправляем изменённый запрос дальше по цепочке
        return chain.proceed(requestBuilder.build())
    }
}
