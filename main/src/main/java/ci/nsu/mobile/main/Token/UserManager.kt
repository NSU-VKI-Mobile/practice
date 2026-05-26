package ci.nsu.mobile.main.Token

import android.content.Context
import android.content.SharedPreferences

object UserManager {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    var currentUserId: Long
        get() = prefs.getLong("current_user_id", -1L)
        set(value) {
            prefs.edit().putLong("current_user_id", value).apply()
        }

    var currentUserLogin: String?
        get() = prefs.getString("current_user_login", null)
        set(value) {
            prefs.edit().putString("current_user_login", value).apply()
        }

    fun clear() {
        prefs.edit().remove("current_user_id").remove("current_user_login").apply()
    }

    fun isLoggedIn(): Boolean = currentUserId != -1L
}