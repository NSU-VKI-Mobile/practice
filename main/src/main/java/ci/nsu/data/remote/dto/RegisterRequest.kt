package ci.nsu.data.remote.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
