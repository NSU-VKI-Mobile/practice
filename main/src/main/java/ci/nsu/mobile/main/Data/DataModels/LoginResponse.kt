package ci.nsu.mobile.main.Data.DataModels

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)