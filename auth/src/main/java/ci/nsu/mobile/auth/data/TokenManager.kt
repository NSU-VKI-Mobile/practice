package ci.nsu.mobile.auth.data

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "userId"
    private const val KEY_LOGIN = "login" // Добавим сохранение логина

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) {
            prefs.edit().putString(KEY_TOKEN, value).apply()
        }

    var userId: Long?
        get() {
            return if (prefs.contains(KEY_USER_ID)) prefs.getLong(KEY_USER_ID, -1) else null
        }
        set(value) {
            prefs.edit().putLong(KEY_USER_ID, value ?: -1).apply()
        }

    var login: String?
        get() = prefs.getString(KEY_LOGIN, null)
        set(value) {
            prefs.edit().putString(KEY_LOGIN, value).apply()
        }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = token != null
}