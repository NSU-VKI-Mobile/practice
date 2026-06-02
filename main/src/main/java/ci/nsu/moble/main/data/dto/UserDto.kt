package ci.nsu.moble.main.data.dto

data class UserDto(
    val userId: Int? = null,
    val login: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val roleId: Int? = null,
    val authAllowed: Boolean? = null
)