package ci.nsu.mobile.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
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