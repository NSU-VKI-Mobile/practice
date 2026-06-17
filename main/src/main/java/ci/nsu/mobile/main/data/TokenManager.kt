package ci.nsu.mobile.main.data

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs?.getString("jwt_token", null)
        set(value) {
            prefs?.edit()?.putString("jwt_token", value)?.apply()
        }

    fun clear() {
        prefs?.edit()?.remove("jwt_token")?.apply()
    }
}