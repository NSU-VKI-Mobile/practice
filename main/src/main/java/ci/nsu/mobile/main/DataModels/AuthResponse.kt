package ci.nsu.mobile.main.DataModels

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)