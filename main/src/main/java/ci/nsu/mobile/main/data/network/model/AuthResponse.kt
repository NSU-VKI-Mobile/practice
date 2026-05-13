package ci.nsu.mobile.main.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
