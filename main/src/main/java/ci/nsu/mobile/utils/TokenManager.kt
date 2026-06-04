package ci.nsu.mobile.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_LOGIN = "user_login"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    var userLogin: String
        get() = prefs.getString(KEY_USER_LOGIN, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USER_LOGIN, value).apply()

    fun clear() {
        prefs.edit().clear().apply()
    }
}