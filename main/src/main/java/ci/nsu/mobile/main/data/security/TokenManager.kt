package ci.nsu.mobile.main.data.security

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"

    private lateinit var prefs: SharedPreferences

    /**
     * Инициализация должна вызываться ОДИН раз до первого сетевого запроса.
     * Рекомендуется вызывать в Application.onCreate() или MainActivity.onCreate().
     */
    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            // Обязательно applicationContext, чтобы избежать утечек памяти при пересоздании Activity
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}