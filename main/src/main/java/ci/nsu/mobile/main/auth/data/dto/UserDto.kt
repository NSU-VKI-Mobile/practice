package ci.nsu.mobile.main.auth.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class UserDto(
    @JsonNames("userId")
    val id: Long,
    val login: String,
    val email: String,
    val phoneNumber: String?,
    @JsonNames("roleId")
    val role: Long,
    val authAllowed: Boolean,
    @JsonNames("personId")
    val personId: Long,
    val createdDate: String,
    val lastLoginDate: String?
)
