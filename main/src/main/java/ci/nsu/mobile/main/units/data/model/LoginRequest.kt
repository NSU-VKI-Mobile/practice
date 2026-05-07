package ci.nsu.mobile.main.units.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)
