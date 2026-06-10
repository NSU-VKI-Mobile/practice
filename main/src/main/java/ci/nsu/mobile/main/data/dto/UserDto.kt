package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int? = null,
    val login: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val roleId: Int? = null,
    val authAllowed: Boolean? = null,
    val personId: Int? = null,
    val createdDate: String? = null,
    val lastLoginDate: String? = null
)