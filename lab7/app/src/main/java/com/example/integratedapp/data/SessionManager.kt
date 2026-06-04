package com.example.integratedapp.data

import android.content.Context
import android.content.SharedPreferences

// SessionManager — хранит данные сессии пользователя
// JWT-токен + ID пользователя
// Это расширенная версия TokenManager из прошлой лабы
//
// Зачем храним userId:
// - Каждый расчёт привязан к пользователю
// - При сохранении расчёта берём userId отсюда
// - При показе истории фильтруем по userId

object SessionManager {

    private const val PREF_NAME = "session_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_LOGIN = "user_login"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) { prefs.edit().putString(KEY_TOKEN, value).apply() }

    // userId — приходит от сервера при логине
    // Используется для привязки расчётов
    var userId: Long
        get() = prefs.getLong(KEY_USER_ID, 0L)
        set(value) { prefs.edit().putLong(KEY_USER_ID, value).apply() }

    var userLogin: String?
        get() = prefs.getString(KEY_LOGIN, null)
        set(value) { prefs.edit().putString(KEY_LOGIN, value).apply() }

    // Проверка — авторизован ли пользователь
    fun isLoggedIn(): Boolean = token != null && userId > 0

    // Очистить всё при выходе из аккаунта
    fun clear() {
        prefs.edit().clear().apply()
    }
}
