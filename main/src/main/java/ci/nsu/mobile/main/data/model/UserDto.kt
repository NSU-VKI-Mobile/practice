package ci.nsu.mobile.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int? = null,
    val login: String,
    val email: String,
    val phoneNumber: String? = null,
    val person: PersonDto? = null
)