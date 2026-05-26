package ci.nsu.mobile.main.Data.DataModels

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)