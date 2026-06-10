package ci.nsu.data.remote.dto

data class AuthResponse(
    val token: String,
    val userId: Long,
    val username: String
)
