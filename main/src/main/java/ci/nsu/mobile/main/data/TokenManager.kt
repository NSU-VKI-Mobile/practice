package ci.nsu.mobile.main.data

import android.content.Context
import android.content.Context.MODE_PRIVATE

class TokenManager(context: Context) {

    companion object {
        private const val PREF_NAME = "auth_prefs"
        private const val KEY_TOKEN = "jwt_token"
    }

    private val prefs = context.applicationContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) {
            prefs.edit().putString(KEY_TOKEN, value).apply()
        }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    fun isLoggedIn(): Boolean = !token.isNullOrEmpty()
}