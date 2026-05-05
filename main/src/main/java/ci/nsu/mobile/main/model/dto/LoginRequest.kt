package ci.nsu.mobile.main.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    @SerialName("login") val username: String,
    @SerialName("password") val password: String
)