package ci.nsu.moble.main.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)
