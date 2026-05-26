package ci.nsu.mobile.main.DataModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val id: Int,
    val login: String,
    val email: String
)