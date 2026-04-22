package ci.nsu.mobile.main.data.local

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private lateinit var prefs: SharedPreferences

    var token: String? = null
        private set

    fun init(context: Context) {
        prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        token = prefs.getString("jwt_token", null)
    }

    fun saveToken(token: String) {
        this.token = token
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun clearToken() {
        this.token = null
        prefs.edit().remove("jwt_token").apply()
    }
}