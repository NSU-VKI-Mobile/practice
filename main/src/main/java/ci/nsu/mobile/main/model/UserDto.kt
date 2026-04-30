package ci.nsu.mobile.main.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String
)