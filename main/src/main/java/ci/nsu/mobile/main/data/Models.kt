@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package ci.nsu.mobile.main.data

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val birthDate: String,
    val gender: String,
    val groupId: Int
)

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

@Serializable
data class LoginRequest(val login: String, val password: String)

@Serializable
data class AuthResponse(val token: String)