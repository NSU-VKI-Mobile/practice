package ci.nsu.mobile.main.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId")
    val id: Int,
    val login: String,
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    val person: PersonDto?
)

