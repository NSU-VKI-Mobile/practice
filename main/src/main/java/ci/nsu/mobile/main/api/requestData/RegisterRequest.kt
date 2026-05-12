package ci.nsu.mobile.main.api.requestData

import ci.nsu.mobile.main.data.dto.PersonDto
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int,
    val authAllowed: Boolean,
    val person: PersonDto
)