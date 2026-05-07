package ci.nsu.mobile.auth.data

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

    var userId: Int
        get() = prefs?.getInt("user_id", -1) ?: -1
        set(value) {
            prefs?.edit()?.putInt("user_id", value)?.apply()
        }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }
}