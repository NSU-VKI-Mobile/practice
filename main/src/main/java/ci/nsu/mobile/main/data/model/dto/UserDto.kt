package ci.nsu.mobile.main.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int? = null,
    val login: String
)