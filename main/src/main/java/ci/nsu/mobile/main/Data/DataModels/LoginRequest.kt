package ci.nsu.mobile.main.Data.DataModels

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)