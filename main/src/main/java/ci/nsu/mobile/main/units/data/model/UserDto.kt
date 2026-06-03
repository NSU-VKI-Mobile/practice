package ci.nsu.mobile.main.units.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val id: Long,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String,
    @SerialName("phoneNumber") val phoneNumber: String? = null,
    @SerialName("roleId") val roleId: Long,
    @SerialName("authAllowed") val authAllowed: Boolean,
    @SerialName("personId") val personId: Long,
    @SerialName("createdDate") val createdDate: String? = null,
    @SerialName("lastLoginDate") val lastLoginDate: String? = null,
)
