package ci.nsu.mobile.main.data.local

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    var token: String?
        get() = sharedPreferences?.getString(KEY_TOKEN, null)
        set(value) {
            sharedPreferences?.edit()?.putString(KEY_TOKEN, value)?.apply()
        }

    fun clearToken() {
        sharedPreferences?.edit()?.remove(KEY_TOKEN)?.apply()
    }
}