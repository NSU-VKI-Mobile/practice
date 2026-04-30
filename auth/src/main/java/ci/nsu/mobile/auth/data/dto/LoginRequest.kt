@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package ci.nsu.mobile.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)
