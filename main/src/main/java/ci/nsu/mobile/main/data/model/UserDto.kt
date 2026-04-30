package ci.nsu.mobile.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String?,
    val phoneNumber: String?,
    val person: PersonDto?,
    val token: String? = null
)

fun UserResponse.toUserDto(token: String? = null): UserDto {
    return UserDto (
        id = this.userId,
        login = this.login,
        email = this.email,
        phoneNumber = this.phoneNumber,
        person = this.personId?.let { PersonDto(id = it)},
        token = token
    )
}