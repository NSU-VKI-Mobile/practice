package ci.nsu.moble.main.data.dto

import kotlinx.serialization.Serializable

// User's login (credentials) data
@Serializable
data class UserDto(
    val userId: Long,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Long,
    val authAllowed: Boolean,
    val personId: Long,
    val createdDate: String,
    val lastLoginDate: String? = null
)