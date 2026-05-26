package ci.nsu.mobile.main.DataModels

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)