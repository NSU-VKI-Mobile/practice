package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val login: String,
    val password: String
)

