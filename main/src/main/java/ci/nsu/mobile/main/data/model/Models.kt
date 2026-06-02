package ci.nsu.mobile.main.data.model


import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val groupId: Int,
    val groupName: String
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
    val phoneNumber: String?,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class UserDto(
    @SerialName("userId") val userId: Long,
    val login: String,
    val email: String
)