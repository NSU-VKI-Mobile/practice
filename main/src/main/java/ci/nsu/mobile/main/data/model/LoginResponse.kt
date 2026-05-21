package ci.nsu.mobile.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)