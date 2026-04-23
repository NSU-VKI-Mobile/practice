package ci.nsu.mobile.main.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int,
    @SerialName("login")
    val login: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phone: String?,
    @SerialName("person")
    val person: PersonDto?,
    @SerialName("roles")
    val roles: List<RoleDto> = emptyList()
)

@Serializable
data class RoleDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)