package ci.nsu.mobile.auth

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS = "auth_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_LOGIN = "user_login"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs?.getString(KEY_TOKEN, null)
        set(value) {
            prefs?.edit()?.putString(KEY_TOKEN, value)?.apply()
        }

    var userId: Long?
        get() = prefs?.getLong(KEY_USER_ID, -1)?.takeIf { it != -1L }
        set(value) {
            prefs?.edit()?.putLong(KEY_USER_ID, value ?: -1)?.apply()
        }

    var userLogin: String?
        get() = prefs?.getString(KEY_USER_LOGIN, null)
        set(value) {
            prefs?.edit()?.putString(KEY_USER_LOGIN, value)?.apply()
        }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }
}