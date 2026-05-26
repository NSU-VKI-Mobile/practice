// ВАЖНО! Убедитесь, что строка package правильная!
package ci.nsu.mobile.main.data.models

import android.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GroupDto(
    @SerialName("groupId")
    val id: Int,
    @SerialName("groupName")
    val name: String
)

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val birthDate: String,
    val gender: String,
    val groupId: Int
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
data class LoginResponse(
    val token: String
 //   val user: UserDto?
)

@Serializable
data class UserDto(
    @SerialName("userId")
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String?,
    val roleId: Int,
    val authAllowed: Boolean,
    val personId: Int,
    val createdDate: String,
    val lastLoginDate: String?
)

@Serializable
data class PersonInfo(
    val firstName: String,
    val lastName: String
)