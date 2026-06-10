package ci.nsu.mobile.main.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(
            "auth_prefs",
            Context.MODE_PRIVATE
        )
    }

    var token: String?
        get() = prefs.getString("token", null)
        set(value) {
            prefs.edit().putString("token", value).apply()
        }

    fun clear() {
        prefs.edit().clear().apply()
    }
}