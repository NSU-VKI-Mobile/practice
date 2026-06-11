package ci.nsu.mobile.main.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class LoginRequest(
    val login: String,
    val password: String
)