package ci.nsu.moble.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId")
    val userId: Int,
    @SerialName("login")
    val login: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String?,
    @SerialName("roleId")
    val roleId: Int,
    @SerialName("authAllowed")
    val authAllowed: Boolean,
    @SerialName("personId")
    val personId: Int? = null,
    @SerialName("createdDate")
    val createdDate: String? = null,
    @SerialName("lastLoginDate")
    val lastLoginDate: String? = null
)