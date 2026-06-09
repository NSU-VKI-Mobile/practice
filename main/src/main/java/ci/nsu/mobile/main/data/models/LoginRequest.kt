package ci.nsu.mobile.main.data.models

data class LoginRequest(
    val login: String,
    val password: String
)

data class LoginResponse(
    val token: String
)