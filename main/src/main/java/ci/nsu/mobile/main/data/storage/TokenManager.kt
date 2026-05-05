package ci.nsu.mobile.main.data.storage

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString("jwt_token", null)
        set(value) {
            prefs.edit().putString("jwt_token", value).apply()
        }

    fun clear() {
        token = null
    }

    fun isLoggedIn(): Boolean {
        return token != null
    }
}