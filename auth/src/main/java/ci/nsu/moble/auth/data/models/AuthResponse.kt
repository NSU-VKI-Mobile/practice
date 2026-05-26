package ci.nsu.moble.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)