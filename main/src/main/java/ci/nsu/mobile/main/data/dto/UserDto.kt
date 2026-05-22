package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId")
    val id: Int,
    @SerialName("login")
    val login: String
)