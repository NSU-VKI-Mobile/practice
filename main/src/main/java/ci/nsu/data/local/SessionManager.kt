package ci.nsu.data.local

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "current_user_id"
        private const val KEY_JWT_TOKEN = "jwt_token"
    }

    // Сохраняем данные после успешного входа/регистрации через API
    fun saveSession(userId: Long, token: String) {
        prefs.edit()
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_JWT_TOKEN, token)
            .apply()
    }

    // Получаем ID пользователя для Room-запросов
    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1L)
    }

    // Получаем токен для AuthInterceptor
    fun getToken(): String? {
        return prefs.getString(KEY_JWT_TOKEN, null)
    }

    // Проверка, авторизован ли пользователь
    fun isUserLoggedIn(): Boolean {
        return getUserId() != -1L && !getToken().isNullOrBlank()
    }

    // Очистка при выходе из аккаунта
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}