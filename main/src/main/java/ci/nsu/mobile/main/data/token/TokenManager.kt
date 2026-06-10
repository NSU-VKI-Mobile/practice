package ci.nsu.mobile.main.data.token

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
    }

    // Сохраняет токен и автоматически вытаскивает из него userId
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        val userId = extractUserIdFromToken(token)
        saveUserId(userId)
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
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1L)
    }

    // Метод парсинга JWT токена
    private fun extractUserIdFromToken(token: String): Long {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return -1L

            // Нам нужна вторая часть (Payload)
            val payloadEncoded = parts[1]
            val decodedBytes = Base64.decode(payloadEncoded, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val payloadString = String(decodedBytes, Charsets.UTF_8)

            // Парсим JSON строку
            val jsonElement = Json.parseToJsonElement(payloadString)
            val jsonObject = jsonElement.jsonObject

            // Ищем ID по трем самым частым стандартам в бэкендах
            jsonObject["id"]?.jsonPrimitive?.longOrNull
                ?: jsonObject["userId"]?.jsonPrimitive?.longOrNull
                ?: jsonObject["sub"]?.jsonPrimitive?.longOrNull
                ?: -1L
        } catch (e: Exception) {
            -1L
        }
    }
}