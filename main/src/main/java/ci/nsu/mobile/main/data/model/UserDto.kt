package ci.nsu.mobile.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId")
    val id: Int? = null,
    val login: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val roleId: Int? = null,
    val authAllowed: Boolean? = null,
    val person: PersonDto? = null
)