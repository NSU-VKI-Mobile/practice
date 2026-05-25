package ci.nsu.moble.main.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String? = null
)