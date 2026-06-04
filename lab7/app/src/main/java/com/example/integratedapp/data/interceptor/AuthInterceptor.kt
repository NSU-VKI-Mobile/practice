package com.example.integratedapp.data.interceptor

import com.example.integratedapp.data.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

// Перехватчик — добавляет JWT-токен к каждому HTTP-запросу
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")

        SessionManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
