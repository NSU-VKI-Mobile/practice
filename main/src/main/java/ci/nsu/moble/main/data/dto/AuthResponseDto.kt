package ci.nsu.moble.main.data.dto

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("token")
    val token: String? = null,

    @SerializedName("user")
    val user: UserDto? = null
)