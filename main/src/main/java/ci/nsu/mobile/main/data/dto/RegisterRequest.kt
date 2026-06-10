package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,          // значение по умолчанию, например, обычный пользователь
    val authAllowed: Boolean = true,
    val person: PersonDto
)