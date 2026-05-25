package ci.nsu.moble.main.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val person: PersonDto?
)