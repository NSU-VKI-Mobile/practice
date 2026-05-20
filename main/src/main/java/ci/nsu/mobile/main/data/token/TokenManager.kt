package ci.nsu.mobile.main.data.token

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"

    private lateinit var prefs: SharedPreferences

    // Вызвать один раз в MainActivity
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) {
            if (value == null) prefs.edit().remove(KEY_TOKEN).apply()
            else prefs.edit().putString(KEY_TOKEN, value).apply()
        }

    fun clear() {
        token = null
    }
}
