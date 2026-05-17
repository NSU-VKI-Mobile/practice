package ci.nsu.mobile.main.api.requestData

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenRespone(
    val token: String
)