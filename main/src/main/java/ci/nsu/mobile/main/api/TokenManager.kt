package ci.nsu.mobile.main.api

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS = "auth_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USERID = "user_id"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        token = "123"
        userId = 1
    }

    var token: String?
        get() = prefs?.getString(KEY_TOKEN , null)
        set(value) {
            prefs?.edit()?.putString(KEY_TOKEN, value)?.apply()
        }
    var userId: Long?
        get() = prefs?.getLong(KEY_USERID , -1)
        set(value) {
            prefs?.edit()?.putLong(KEY_USERID, value?:-1)?.apply()
        }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }
}