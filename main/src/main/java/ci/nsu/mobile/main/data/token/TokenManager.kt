package ci.nsu.mobile.main.data.token

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_LOGIN = "user_login"
    }

    fun saveToken(token: String) {
        Log.d("TokenManager", "saveToken called with token: $token")
        prefs.edit().putString(KEY_TOKEN, token).apply()

        val userLogin = extractUserLoginFromToken(token)
        Log.d("TokenManager", "saveToken: extracted userLogin = $userLogin")
        saveUserLogin(userLogin)

    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun hasToken(): Boolean {
        return prefs.getString(KEY_TOKEN, null) != null
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }

    fun saveUserId(userId: Long) {
        Log.d("TokenManager", "saveUserId: saving $userId")
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Long {
        val id = prefs.getLong(KEY_USER_ID, -1L)
        Log.d("TokenManager", "getUserId() = $id")
        return id
    }

    fun saveUserLogin(login: String) {
        Log.d("TokenManager", "saveUserLogin: saving $login")
        prefs.edit().putString(KEY_USER_LOGIN, login).apply()
    }

    fun getUserLogin(): String {
        val login = prefs.getString(KEY_USER_LOGIN, "") ?: ""
        Log.d("TokenManager", "getUserLogin() = $login")
        return login
    }

    private fun extractUserLoginFromToken(token: String): String {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) {
                Log.e("TokenManager", "Token has less than 2 parts: ${parts.size}")
                return ""
            }

            val payloadEncoded = parts[1]
            val decodedBytes = Base64.decode(payloadEncoded, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val payloadString = String(decodedBytes, Charsets.UTF_8)

            Log.d("TokenManager", "JWT payload: $payloadString")

            val jsonElement = Json.parseToJsonElement(payloadString)
            val jsonObject = jsonElement.jsonObject

            val login = jsonObject["sub"]?.jsonPrimitive?.content ?: ""
            Log.d("TokenManager", "extracted userLogin: $login")
            login
        } catch (e: Exception) {
            Log.e("TokenManager", "Failed to extract userLogin from token", e)
            ""
        }
    }
}