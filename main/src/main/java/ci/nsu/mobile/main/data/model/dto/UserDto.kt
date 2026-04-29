package ci.nsu.mobile.main.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int? = null,
    val login: String
)