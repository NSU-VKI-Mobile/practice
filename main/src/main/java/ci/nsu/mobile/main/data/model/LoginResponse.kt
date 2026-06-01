package ci.nsu.mobile.main.data.model

data class LoginResponse(
    val token: String,
    val user: UserDto
)