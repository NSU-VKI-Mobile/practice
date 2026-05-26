package ci.nsu.mobile.main.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId") val userId: Long,
    @SerializedName("login") val login: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("roleId") val roleId: Int? = null,
    @SerializedName("authAllowed") val authAllowed: Boolean? = null,
    @SerializedName("personId") val personId: Long? = null
)