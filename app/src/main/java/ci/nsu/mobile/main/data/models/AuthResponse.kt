package ci.nsu.mobile.main.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String? = null
)