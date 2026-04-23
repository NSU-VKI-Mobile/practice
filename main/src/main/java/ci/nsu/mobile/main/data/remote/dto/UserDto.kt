package ci.nsu.mobile.main.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String,
    @SerialName("phoneNumber")
    val phone: String?,
    val person: PersonDto?,
    val roles: List<RoleDto> = emptyList()
)

@Serializable
data class RoleDto(
    val id: Int,
    val name: String
)