package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable

// User's login (credentials) data
@Serializable
data class UserDto(
    val userId: Long,
    val login: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val roleId: Long? = null,
    val authAllowed: Boolean? = null,
    val personId: Long? = null,
    val createdDate: String? = null,
    val lastLoginDate: String? = null
)