package ci.nsu.mobile.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId")
    val id: Int = 0,
    @SerialName("groupName")
    val name: String = ""
)

@Serializable
data class PersonDto(
    @SerialName("personId")
    val id: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val groupId: Int? = null,
    @SerialName("groupName")
    val groupName: String? = null
)

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class UserDto(
    @SerialName("userId")
    val userId: Int? = null,
    val id: Int? = null,
    val login: String = "",
    val email: String? = null,
    val phoneNumber: String? = null,
    val roleId: Int? = null,
    val authAllowed: Boolean? = null,
    val token: String? = null,
    val jwt: String? = null,
    val accessToken: String? = null,
    @SerialName("access_token")
    val accessTokenSnake: String? = null,
    @SerialName("jwtToken")
    val jwtToken: String? = null,
    val person: PersonDto? = null
)

@Serializable
data class LoginResponse(
    val token: String? = null,
    val jwt: String? = null,
    val accessToken: String? = null,
    @SerialName("access_token")
    val accessTokenSnake: String? = null,
    @SerialName("jwtToken")
    val jwtToken: String? = null,
    val user: UserDto? = null,
    @SerialName("userId")
    val userId: Int? = null,
    val id: Int? = null,
    val login: String = "",
    val email: String? = null,
    val phoneNumber: String? = null,
    val roleId: Int? = null,
    val authAllowed: Boolean? = null,
    val person: PersonDto? = null
)

fun UserDto.extractToken(): String? = token ?: jwt ?: accessToken ?: accessTokenSnake ?: jwtToken

fun LoginResponse.extractToken(): String? = token ?: jwt ?: accessToken ?: accessTokenSnake ?: jwtToken

fun LoginResponse.toUserDto(): UserDto {
    val nestedUser = user
    return if (nestedUser != null) {
        nestedUser.copy(
            token = nestedUser.token ?: token,
            jwt = nestedUser.jwt ?: jwt,
            accessToken = nestedUser.accessToken ?: accessToken,
            accessTokenSnake = nestedUser.accessTokenSnake ?: accessTokenSnake,
            jwtToken = nestedUser.jwtToken ?: jwtToken
        )
    } else {
        UserDto(
            userId = userId,
            id = id,
            login = login,
            email = email,
            phoneNumber = phoneNumber,
            roleId = roleId,
            authAllowed = authAllowed,
            token = token,
            jwt = jwt,
            accessToken = accessToken,
            accessTokenSnake = accessTokenSnake,
            jwtToken = jwtToken,
            person = person
        )
    }
}

fun UserDto.displayName(): String {
    val personName = person?.let {
        listOf(it.lastName, it.firstName, it.middleName)
            .filter { part -> part.isNotBlank() }
            .joinToString(" ")
    }.orEmpty()

    return personName.ifBlank {
        login.ifBlank {
            "User #${userId ?: id ?: "?"}"
        }
    }
}

fun UserDto.details(): String {
    return listOfNotNull(
        login.takeIf { it.isNotBlank() }?.let { "login: $it" },
        email?.takeIf { it.isNotBlank() },
        phoneNumber?.takeIf { it.isNotBlank() },
        person?.groupName?.takeIf { it.isNotBlank() }
    ).joinToString(" | ")
}
