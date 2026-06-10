package ci.nsu.mobile.main.data.local

import android.content.Context
import androidx.core.content.edit


class TokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
    }
    fun saveToken(token: String) { prefs.edit { putString(KEY_TOKEN, token) } }
    fun getToken(): String? { return prefs.getString(KEY_TOKEN, null) }

    fun saveUserId(id: String) { prefs.edit { putString(KEY_USER_ID, id) } }
    fun getUserId(): String? { return prefs.getString(KEY_USER_ID, null) }

    fun clearSession() { prefs.edit { clear() } }
}