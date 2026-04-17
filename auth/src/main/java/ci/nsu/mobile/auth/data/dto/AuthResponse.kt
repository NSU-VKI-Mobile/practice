package ci.nsu.mobile.auth.data.dto

import kotlinx.serialization.Serializable
@Serializable
data class AuthResponse(
    val token: String
)
