package ci.nsu.mobile.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)
