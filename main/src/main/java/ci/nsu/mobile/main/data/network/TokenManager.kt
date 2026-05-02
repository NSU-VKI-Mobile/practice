package ci.nsu.mobile.main.data.network

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "auth_preferences"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"

    private var preferences: SharedPreferences? = null

    fun init(context: Context) {
        if (preferences == null) {
            preferences = context.applicationContext.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
        }
    }

    var token: String?
        get() = preferences?.getString(KEY_TOKEN, null)
        set(value) {
            preferences?.edit()?.apply {
                if (value.isNullOrBlank()) {
                    remove(KEY_TOKEN)
                } else {
                    putString(KEY_TOKEN, value)
                }
                apply()
            }
        }

    var userId: Long?
        get() = preferences?.takeIf { it.contains(KEY_USER_ID) }?.getLong(KEY_USER_ID, 0L)
        set(value) {
            preferences?.edit()?.apply {
                if (value == null || value <= 0L) {
                    remove(KEY_USER_ID)
                } else {
                    putLong(KEY_USER_ID, value)
                }
                apply()
            }
        }

    fun clear() {
        token = null
        userId = null
    }
}
