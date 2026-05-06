package ci.nsu.mobile.main.data

import android.content.Context

class TokenManager(context: Context) {
    private val preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    var token: String?
        get() = preferences.getString(KEY_TOKEN, null)
        set(value) {
            preferences.edit().putString(KEY_TOKEN, value).apply()
        }

    fun clear() {
        preferences.edit().remove(KEY_TOKEN).apply()
    }

    companion object {
        private const val KEY_TOKEN = "jwt_token"
    }
}
