package ci.nsu.moble.main.data

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val PREF_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(TOKEN_KEY, null)
        set(value) {
            prefs.edit().putString(TOKEN_KEY, value).apply()
        }

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}