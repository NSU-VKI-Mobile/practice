package ci.nsu.mobile.main.auth.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val json = Json { ignoreUnknownKeys = true }

    var token: String?
        get() = prefs.getString("auth_token", null)
        set(value) = prefs.edit { putString("auth_token", value) }

    var userId: Long?
        get() = if (prefs.contains("user_id")) prefs.getLong("user_id", -1L) else null
        set(value) {
            if (value != null) {
                prefs.edit { putLong("user_id", value) }
            } else {
                prefs.edit { remove("user_id") }
            }
        }

    // Извлекаем login из JWT токена
    fun getUserLoginFromToken(): String? {
        val currentToken = token ?: return null

        return try {
            // Разделяем JWT на части
            val parts = currentToken.split(".")
            if (parts.size != 3) return null

            // Декодируем payload (вторую часть)
            val payloadJson = decodeBase64ToJson(parts[1])

            // Парсим JSON с помощью kotlinx.serialization
            val jsonElement = json.parseToJsonElement(payloadJson)
            val login = jsonElement.jsonObject["sub"]?.jsonPrimitive?.content

            login
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun decodeBase64ToJson(base64: String): String {
        var cleaned = base64
            .replace('-', '+')
            .replace('_', '/')

        while (cleaned.length % 4 != 0) { //paddings something
            cleaned += "="
        }

        val decodedBytes = android.util.Base64.decode(cleaned, android.util.Base64.DEFAULT)
        return String(decodedBytes, Charsets.UTF_8)
    }

    fun saveUserId(id: Long) {
        userId = id
    }

    fun clear() {
        prefs.edit { clear() }
    }
}