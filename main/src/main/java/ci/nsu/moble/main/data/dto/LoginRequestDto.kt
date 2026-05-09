package ci.nsu.moble.main.data.dto

import kotlinx.serialization.Serializable

// When logging in we give server credentials
@Serializable
data class LoginRequestDto(
    val login: String,
    val password: String
)