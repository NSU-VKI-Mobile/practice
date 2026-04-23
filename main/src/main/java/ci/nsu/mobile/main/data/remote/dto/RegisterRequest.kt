package ci.nsu.mobile.main.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("roleId")
    val roleId: Int = 1,
    @SerialName("authAllowed")
    val authAllowed: Boolean = true,
    @SerialName("person")
    val person: PersonDto
)