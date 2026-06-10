package ci.nsu.mobile.main.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("roleId")
    val roleId: Int = 1,
    @SerializedName("authAllowed")
    val authAllowed: Boolean = true,
    val person: PersonDto
)

