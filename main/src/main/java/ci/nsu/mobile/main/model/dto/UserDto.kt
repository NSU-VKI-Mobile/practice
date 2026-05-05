package ci.nsu.mobile.main.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val personId: Int,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
)