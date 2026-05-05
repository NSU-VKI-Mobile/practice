package ci.nsu.mobile.main.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String? = null,
    @SerialName("phoneNumber") val phone: String? = null,
    @SerialName("person") val person: PersonDto? = null
)