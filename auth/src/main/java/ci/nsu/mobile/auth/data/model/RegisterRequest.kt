package ci.nsu.mobile.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1, // По заданию всегда 1
    val authAllowed: Boolean = true,
    val person: PersonDto
)