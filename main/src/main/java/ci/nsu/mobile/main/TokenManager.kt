package ci.nsu.mobile.main

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val PREFS = "auth_prefs"
    private const val KEY = "token"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs?.getString(KEY, null)
        set(value) {
            prefs?.edit()?.putString(KEY, value)?.apply()
        }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }
}