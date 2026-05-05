package ci.nsu.mobile.main.data.local

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"

    var token: String? = null
        private set

    fun init(context: Context) {
        if (::prefs.isInitialized) return
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        token = prefs.getString(TOKEN_KEY, null)
    }

    fun saveToken(token: String) {
        this.token = token
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun clearToken() {
        this.token = null
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}
