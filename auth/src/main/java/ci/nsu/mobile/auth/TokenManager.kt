package ci.nsu.mobile.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

object TokenManager {
    private const val PREFS = "auth_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs?.getString(KEY_TOKEN, null)
        set(value) {
            prefs?.edit()?.putString(KEY_TOKEN, value)?.apply()
            userId = value?.let { parseUserIdFromJwt(it) }
        }

    var userId: Int?
        get() {
            val id = prefs?.getInt(KEY_USER_ID, -1) ?: return null
            return if (id == -1) null else id
        }
        set(value) {
            if (value != null) prefs?.edit()?.putInt(KEY_USER_ID, value)?.apply()
            else prefs?.edit()?.remove(KEY_USER_ID)?.apply()
        }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
        prefs?.edit()?.remove(KEY_TOKEN)?.remove(KEY_USER_ID)?.apply()
    }

    private fun parseUserIdFromJwt(token: String): Int? = runCatching {
        val payload = token.split(".")[1]
        val json = JSONObject(
            String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING))
        )
        when {
            json.has("userId") -> json.getInt("userId")
            json.has("id") -> json.getLong("id").toInt()   // Long → Int, без as-каста
            json.has("sub") -> json.getString("sub").toIntOrNull()
            else -> null
        }
    }.getOrNull()
}