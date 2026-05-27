package ci.nsu.mobile.main.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val id: Int? = null,
    val login: String,
    val email: String,
    val phoneNumber: String? = null,
    val person: PersonDto? = null
)
