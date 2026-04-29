package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val token: String
)
