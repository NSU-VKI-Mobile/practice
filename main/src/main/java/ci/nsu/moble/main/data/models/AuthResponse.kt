package ci.nsu.moble.main.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String? = null
)