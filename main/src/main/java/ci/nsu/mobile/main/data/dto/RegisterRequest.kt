package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String? = null,
    val roleId: Int = 1,  // Always 1, not changeable
    val authAllowed: Boolean = true,
    val person: PersonDto
)