package ci.nsu.mobile.main.model

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int,
    val authAllowed: Boolean,
    val person: PersonDto
)