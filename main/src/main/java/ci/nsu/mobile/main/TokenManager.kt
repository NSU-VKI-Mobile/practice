package ci.nsu.mobile.main

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

object TokenManager {

    private const val PREFS     = "auth_prefs"
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

    var userId: Long?
        get() {
            val id = prefs?.getLong(KEY_USER_ID, -1L) ?: return null
            return if (id == -1L) null else id
        }
        set(value) {
            if (value != null)
                prefs?.edit()?.putLong(KEY_USER_ID, value)?.apply()
            else
                prefs?.edit()?.remove(KEY_USER_ID)?.apply()
        }

    fun clear() {
        prefs?.edit()?.clear()?.apply()
    }

    private fun parseUserIdFromJwt(token: String): Long? = runCatching {
        val payload = token.split(".")[1]
        val json = JSONObject(
            String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING))
        )
        when {
            json.has("userId") -> json.getLong("userId")
            json.has("id")     -> json.getLong("id")
            json.has("sub")    -> json.getString("sub").toLongOrNull()
            else               -> null
        }
    }.getOrNull()
}