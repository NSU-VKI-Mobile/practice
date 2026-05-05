package ci.nsu.mobile.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)