package ci.nsu.moble.main.data.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    var token: String?
        get() {
            val t = prefs.getString(KEY_TOKEN, null)
            Log.d("TokenManager", "Getting token: ${if (t != null) "present" else "null"}")
            return t
        }
        set(value) {
            if (value == null) {
                Log.d("TokenManager", "Clearing token")
                prefs.edit().remove(KEY_TOKEN).apply()
            } else {
                Log.d("TokenManager", "Saving token: $value")
                prefs.edit().putString(KEY_TOKEN, value).apply()
            }
        }

    fun clear() {
        token = null
    }

    companion object {
        private const val KEY_TOKEN = "jwt_token"
    }
}