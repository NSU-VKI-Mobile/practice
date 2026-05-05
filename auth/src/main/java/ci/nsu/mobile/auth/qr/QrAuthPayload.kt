package ci.nsu.mobile.auth.qr

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class QrAuthPayload(
    val login: String,
    val password: String
) {
    fun isValid(): Boolean = login.isNotBlank() && password.isNotBlank()

    fun toJson(): String = json.encodeToString(serializer(), this)

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromRaw(rawValue: String): QrAuthPayload? {
            return parseJson(rawValue) ?: parseSeparatedString(rawValue)
        }

        private fun parseJson(rawValue: String): QrAuthPayload? {
            return runCatching {
                json.decodeFromString(serializer(), rawValue)
            }.getOrNull()?.takeIf { it.isValid() }
        }

        private fun parseSeparatedString(rawValue: String): QrAuthPayload? {
            val trimmedValue = rawValue.trim()
            if (trimmedValue.startsWith("{") || trimmedValue.startsWith("[")) {
                return null
            }

            val separatorIndex = trimmedValue.indexOf(':')
            if (separatorIndex <= 0 || separatorIndex == trimmedValue.lastIndex) {
                return null
            }

            return QrAuthPayload(
                login = trimmedValue.substring(0, separatorIndex),
                password = trimmedValue.substring(separatorIndex + 1)
            ).takeIf { it.isValid() }
        }
    }
}
