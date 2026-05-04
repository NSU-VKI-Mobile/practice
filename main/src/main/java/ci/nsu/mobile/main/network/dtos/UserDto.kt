package ci.nsu.mobile.main.network.dtos

data class UserDto(
    val person: PersonDto,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val roleId: Int = 1
)