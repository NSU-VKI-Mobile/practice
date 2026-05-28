package com.example.authapp.data.api

import com.example.authapp.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// ApiService — интерфейс с описанием всех запросов к серверу
// Retrofit сам создаст реализацию — мы описываем только ЧТО хотим
//
// Каждая функция = один HTTP-запрос:
// @GET — запрос на чтение данных
// @POST — запрос на отправку данных
// @Body — тело запроса (JSON)
// Response<T> — ответ сервера с типом T

interface ApiService {

    // POST /auth/login — вход в систему
    // Отправляем LoginRequest (логин + пароль)
    // Получаем UserDto (данные пользователя + JWT-токен)
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    // POST /auth/register — регистрация
    // Отправляем RegisterRequest (все данные нового пользователя)
    // Получаем Unit (ничего — просто факт что регистрация прошла)
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    // GET /users — получить список всех пользователей
    // Требует авторизации (AuthInterceptor добавит токен)
    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    // GET /groups — получить список групп
    // Нужен для выпадающего списка при регистрации
    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>
}
