package ci.nsu.mobile.main.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int? = null,
    val login: String
)