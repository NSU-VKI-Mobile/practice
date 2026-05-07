package ci.nsu.mobile.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int,
    val login: String,
    val email: String,
    val phoneNumber: String? = null,
    val roleId: Int,
    val authAllowed: Boolean,
    val personId: Int,
    val createdDate: String? = null,
    val lastLoginDate: String? = null
)