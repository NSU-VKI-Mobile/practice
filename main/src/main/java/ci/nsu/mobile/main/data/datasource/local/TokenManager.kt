package ci.nsu.mobile.main.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString("auth_token", null)
        set(value) = prefs.edit { putString("auth_token", value) }

    fun clear() {
        prefs.edit { clear() }
    }
}