package ci.nsu.mobile.main.data.local

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val LOGIN_KEY = "user_login"
    private var prefs: SharedPreferences? = null

    // Эту функцию мы вызовем один раз при старте приложения (в MainActivity)
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs?.getString(TOKEN_KEY, null)
        set(value) {
            prefs?.edit()?.putString(TOKEN_KEY, value)?.apply()
        }

    var login: String?
        get() = prefs?.getString(LOGIN_KEY, null)
        set(value) {
            prefs?.edit()?.putString(LOGIN_KEY, value)?.apply()
        }

    fun clear() {
        prefs?.edit()?.remove(TOKEN_KEY)?.remove(LOGIN_KEY)?.apply()
    }
}