package ci.nsu.moble.auth.data.storage

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context)
{
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun saveUserId(userId: Long) {
        prefs.edit().putLong("user_id", userId).apply()
    }

    fun getUserId(): Long? {
        val id = prefs.getLong("user_id", -1L)
        return if (id == -1L) null else id
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null && getUserId() != null
    }
}