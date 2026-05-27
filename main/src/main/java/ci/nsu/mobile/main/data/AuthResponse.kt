package ci.nsu.mobile.main.data

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)