package ci.nsu.mobile.main.data.network

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object TokenManager {

    private lateinit var prefs: SharedPreferences

    private const val PREF_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val USER_LOGIN_KEY = "user_login"
    private const val USER_ID_KEY = "user_id"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(TOKEN_KEY, null)
        set(value) {
            prefs.edit { putString(TOKEN_KEY, value) }
        }

    var userLogin: String?
        get() = prefs.getString(USER_LOGIN_KEY, null)
        set(value) {
            prefs.edit { putString(USER_LOGIN_KEY, value) }
        }

    var userId: Int
        get() = prefs.getInt(USER_ID_KEY, -1)
        set(value) {
            prefs.edit { putInt(USER_ID_KEY, value) }
        }

    fun clear() {
        prefs.run { edit().clear().apply() }
    }

    fun isLoggedIn(): Boolean {
        return !token.isNullOrEmpty()
    }
}