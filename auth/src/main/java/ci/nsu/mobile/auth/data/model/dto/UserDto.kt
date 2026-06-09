package ci.nsu.mobile.auth.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int? = null,
    val login: String
)