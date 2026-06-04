package ci.nsu.mobile.main.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId")
    val id: Int,

    val login: String,

    val email: String? = null,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    val roleId: Int? = null,

    val authAllowed: Boolean? = null,

    val personId: Int? = null,

    val createdDate: String? = null,

    val lastLoginDate: String? = null,

    val token: String? = null
)