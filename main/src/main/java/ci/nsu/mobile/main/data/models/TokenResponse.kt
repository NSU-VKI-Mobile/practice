package ci.nsu.mobile.main.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)