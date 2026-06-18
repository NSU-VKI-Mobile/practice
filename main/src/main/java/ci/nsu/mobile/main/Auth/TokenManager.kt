package ci.nsu.mobile.main.Auth

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val TOKEN_KEY = "jwt_token"

    var token: String?
        get() = prefs.getString(TOKEN_KEY, null)
        set(value) {
            if (value == null) {
                prefs.edit().remove(TOKEN_KEY).apply()
            } else {
                prefs.edit().putString(TOKEN_KEY, value).apply()
            }
        }

    fun clear() {
        token = null
    }
}