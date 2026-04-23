package ci.nsu.mobile.main.data.model.request

import ci.nsu.mobile.main.data.model.request.PersonDto
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