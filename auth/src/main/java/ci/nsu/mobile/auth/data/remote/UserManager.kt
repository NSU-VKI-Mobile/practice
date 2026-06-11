package ci.nsu.mobile.auth.data.remote

import android.content.Context
import android.content.SharedPreferences

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var userId: Long
        get() = prefs.getLong(KEY_USER_ID, -1L)
        set(value) { prefs.edit().putLong(KEY_USER_ID, value).apply() }

    fun clearUser() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    fun isLoggedIn(): Boolean = userId != -1L
}