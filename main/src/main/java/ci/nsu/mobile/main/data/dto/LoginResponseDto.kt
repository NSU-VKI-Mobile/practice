package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable

// When logging in, server gives us a token
@Serializable
data class LoginResponseDto(
    val token: String
)