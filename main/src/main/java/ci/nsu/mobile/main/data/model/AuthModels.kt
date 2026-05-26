package ci.nsu.mobile.main.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String
)

data class RegisterRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("roleId") val roleId: Int = 1,
    @SerializedName("authAllowed") val authAllowed: Boolean = true,
    @SerializedName("person") val person: PersonDto
)

data class PersonDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("middleName") val middleName: String? = null,
    @SerializedName("birthDate") val birthDate: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("groupId") val groupId: Int
)