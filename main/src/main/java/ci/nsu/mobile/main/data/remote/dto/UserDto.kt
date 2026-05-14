package ci.nsu.mobile.main.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("login")
    val login: String? = "",
    @SerialName("email")
    val email: String? = "",
    @SerialName("phoneNumber")
    val phone: String? = null,
    @SerialName("person")
    val person: PersonDto? = null,
    @SerialName("roles")
    val roles: List<RoleDto> = emptyList()
)

@Serializable
data class RoleDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = ""
)