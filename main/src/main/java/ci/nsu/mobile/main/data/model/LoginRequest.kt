@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)