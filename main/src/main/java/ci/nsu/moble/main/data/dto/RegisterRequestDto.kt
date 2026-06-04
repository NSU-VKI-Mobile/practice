package ci.nsu.moble.main.data.dto

import kotlinx.serialization.Serializable

// Registering user login (credentials) data and their personal data
@Serializable
data class RegisterRequestDto(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto // personal data
)