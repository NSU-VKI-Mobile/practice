package ci.nsu.mobile.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class QrAuthData(
    val login: String,
    val password: String
) {
    fun toJson(): String = Json.encodeToString(serializer(), this)

    fun toSimpleString(): String = "$login:$password"

    companion object {
        fun fromJson(json: String): QrAuthData? {
            return try {
                Json.decodeFromString(serializer(), json)
            } catch (e: Exception) {
                null
            }
        }

        fun fromSimpleString(data: String): QrAuthData? {
            val parts = data.split(":")
            return if (parts.size == 2) {
                QrAuthData(parts[0], parts[1])
            } else null
        }
    }
}