package com.example.authapp.data

import android.content.Context
import android.content.SharedPreferences

// TokenManager — хранит JWT-токен в SharedPreferences
// SharedPreferences — простое хранилище ключ-значение на устройстве
// Даже после закрытия приложения токен сохраняется
//
// JWT (JSON Web Token) — это строка которую сервер даёт при логине
// Потом при каждом запросе мы отправляем эту строку в заголовке
// Сервер видит токен и понимает: "это авторизованный пользователь"
//
// Аналогия: JWT = пропуск в здание. Охранник (сервер) видит пропуск и пускает.

object TokenManager {

    // object — синглтон в Kotlin, один экземпляр на всё приложение
    private const val PREF_NAME = "auth_prefs"  // имя файла настроек
    private const val KEY_TOKEN = "jwt_token"    // ключ для токена

    private lateinit var prefs: SharedPreferences
    // lateinit — инициализируем позже (в init), а не сразу

    // Вызываем один раз при старте приложения
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        // MODE_PRIVATE — только наше приложение может читать этот файл
    }

    // Геттер и сеттер для токена
    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)  // прочитать из хранилища
        set(value) {
            // записать в хранилище
            prefs.edit().putString(KEY_TOKEN, value).apply()
            // apply() — сохранить асинхронно (не блокирует поток)
        }

    // Удалить токен (при выходе из аккаунта)
    fun clear() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}
