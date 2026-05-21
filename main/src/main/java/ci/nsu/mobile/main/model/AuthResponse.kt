package ci.nsu.mobile.main.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)