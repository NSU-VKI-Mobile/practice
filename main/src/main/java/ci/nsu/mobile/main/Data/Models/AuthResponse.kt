package ci.nsu.mobile.main.Data.Models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)