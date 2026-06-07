package ci.nsu.mobile.auth.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
