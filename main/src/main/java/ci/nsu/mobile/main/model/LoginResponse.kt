package ci.nsu.mobile.main.model

data class LoginResponse(
    val token: String,
    val user: UserDto
)

