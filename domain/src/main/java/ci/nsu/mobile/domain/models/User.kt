package ci.nsu.mobile.domain.models

data class User(
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)