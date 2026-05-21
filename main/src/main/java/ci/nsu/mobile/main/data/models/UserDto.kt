package ci.nsu.mobile.main.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId")
    val userId: Int,
    @SerialName("login")
    val login: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String? = null
)

@Serializable
data class PersonDto(
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("middleName")
    val middleName: String,
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("groupId")
    val groupId: Int
)

@Serializable
data class RegisterRequest(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("roleId")
    val roleId: Int = 1,
    @SerialName("authAllowed")
    val authAllowed: Boolean = true,
    @SerialName("person")
    val person: PersonDto
)

@Serializable
data class LoginRequest(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String
)