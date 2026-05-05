package ci.nsu.mobile.main.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)