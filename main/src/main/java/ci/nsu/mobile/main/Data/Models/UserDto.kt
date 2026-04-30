package ci.nsu.mobile.main.Data.Models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val person: PersonDto
)