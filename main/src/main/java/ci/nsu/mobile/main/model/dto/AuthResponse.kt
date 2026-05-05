package ci.nsu.mobile.main.model.dto

import kotlinx.serialization.Serializable
@Serializable
data class AuthResponse(
    val token: String
)