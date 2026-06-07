package ci.nsu.mobile.auth.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String? = null,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)