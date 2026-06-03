package ci.nsu.mobile.main.units.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("roleId") val roleId: Int,
    @SerialName("person") val person: PersonDto? = null
)
