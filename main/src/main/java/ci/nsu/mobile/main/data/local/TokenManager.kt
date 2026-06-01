package ci.nsu.mobile.main.data.local

import android.content.Context

object TokenManager {

    private const val PREF_NAME = "auth_pref"
    private const val TOKEN_KEY = "jwt"

    private var prefs =
        null as android.content.SharedPreferences?

    fun init(context: Context) {

        prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    var token: String?
        get() = prefs?.getString(TOKEN_KEY, null)

        set(value) {
            prefs?.edit()
                ?.putString(TOKEN_KEY, value)
                ?.apply()
        }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }
}