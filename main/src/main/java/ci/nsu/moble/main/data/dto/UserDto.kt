package ci.nsu.moble.main.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val id: Int,
    val login: String,
    val email: String,
    @SerialName("phoneNumber") val phoneNumber: String? = null
)