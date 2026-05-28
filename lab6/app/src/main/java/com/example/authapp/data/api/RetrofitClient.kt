package com.example.authapp.data.api

import com.example.authapp.data.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// RetrofitClient — синглтон для создания Retrofit-клиента
// Retrofit — библиотека которая превращает интерфейс ApiService в реальные HTTP-запросы
//
// Цепочка: ApiService (что хотим) → Retrofit (как отправить) → OkHttp (транспорт) → Сервер

object RetrofitClient {

    // Адрес сервера — все запросы пойдут сюда
    private const val BASE_URL = "http://192.168.200.160:8080/api/"

    // Логирование — показывает запросы/ответы в Logcat для отладки
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        // Level.BODY — логируем всё: URL, заголовки, тело запроса и ответа
    }

    // OkHttpClient — "транспорт" для HTTP-запросов
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())       // добавляем токен к каждому запросу
        .addInterceptor(loggingInterceptor)      // логируем для отладки
        .connectTimeout(30, TimeUnit.SECONDS)    // таймаут подключения
        .readTimeout(30, TimeUnit.SECONDS)       // таймаут чтения ответа
        .build()

    // Retrofit — собирает всё вместе
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)                                      // базовый URL
        .client(okHttpClient)                                   // транспорт с интерсепторами
        .addConverterFactory(GsonConverterFactory.create())     // JSON → Kotlin объекты
        .build()

    // Создаём реализацию ApiService
    // Retrofit сам напишет код для всех функций интерфейса
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
