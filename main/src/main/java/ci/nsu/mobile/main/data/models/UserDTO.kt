package ci.nsu.mobile.main.data.models

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("email") val email: String,
    @SerializedName("phoneNumber") val phoneNumber: String?,
    @SerializedName("person") val person: PersonDto?
)
