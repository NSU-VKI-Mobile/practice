package ci.nsu.mobile.main.data.model

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
    val phoneNumber: String? = null,

    @SerialName("roleId")
    val roleId: Int,

    @SerialName("authAllowed")
    val authAllowed: Boolean,

    @SerialName("personId")
    val personId: Int,

    @SerialName("createdDate")
    val createdDate: String,

    @SerialName("lastLoginDate")
    val lastLoginDate: String? = null
)