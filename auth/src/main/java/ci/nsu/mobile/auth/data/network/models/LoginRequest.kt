package ci.nsu.mobile.auth.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)