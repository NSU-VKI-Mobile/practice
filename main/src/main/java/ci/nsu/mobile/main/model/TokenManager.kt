package ci.nsu.mobile.main.model

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) {
            if (value == null) {
                prefs.edit().remove(KEY_TOKEN).apply()
            } else {
                prefs.edit().putString(KEY_TOKEN, value).apply()
            }
        }

    fun clear() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    companion object {
        private const val KEY_TOKEN = "auth_token"
    }
}