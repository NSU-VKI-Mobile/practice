package ci.nsu.mobile.main.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int,
    val person: PersonDto,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1
)