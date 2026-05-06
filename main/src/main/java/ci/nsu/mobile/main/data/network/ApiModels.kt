package ci.nsu.mobile.main.data.network

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("groupId")
    val id: Int,
    @SerializedName("groupName")
    val name: String
)

data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String,
    val gender: String,
    val groupId: Int
)

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

data class LoginRequest(
    val login: String,
    val password: String
)

data class UserDto(
    val id: Int? = null,
    val login: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val person: PersonDto? = null,
    val token: String? = null
) {
    val displayName: String
        get() {
            val fullName = listOfNotNull(
                person?.lastName,
                person?.firstName,
                person?.middleName
            ).filter { it.isNotBlank() }.joinToString(" ")
            return fullName.ifBlank { login ?: email ?: "Пользователь #${id ?: "-"}" }
        }
}
