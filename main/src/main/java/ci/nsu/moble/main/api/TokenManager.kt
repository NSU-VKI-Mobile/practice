package ci.nsu.moble.main.api

import android.content.Context
import androidx.core.content.edit

/**
 * Manages token within system storage
 */
class TokenManager(context: Context) {
    // SharedPreferences - some kind of system local storage (key:value)
    // "auth_prefs" - filename
    // MODE_PRIVATE - only this app can access it
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString("jwt_token", null)
        set(value) = prefs.edit { putString("jwt_token", value) }

    fun clear() = prefs.edit { remove("jwt_token") }
}