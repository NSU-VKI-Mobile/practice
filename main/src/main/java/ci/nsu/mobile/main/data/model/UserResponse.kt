package ci.nsu.mobile.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: Int,
    val login: String,
    val email: String?,
    val phoneNumber: String?,
    val personId: Int?,
    val roleId: Int?,
    val authAllowed: Boolean?,
    val createdDate: String?,
    val lasloginDate: String?,
)
