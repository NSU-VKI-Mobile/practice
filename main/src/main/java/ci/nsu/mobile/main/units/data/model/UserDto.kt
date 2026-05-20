package ci.nsu.mobile.main.units.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int,
    val person: PersonDto? = null
)
