package ci.nsu.mobile.main.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int,
    val login: String,
    val email: String,
    val phoneNumber: String? = null,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val personId: Int,
    val createdDate: String,
    val lastLoginDate: String? = null
)