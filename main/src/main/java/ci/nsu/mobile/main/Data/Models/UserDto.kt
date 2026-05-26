package ci.nsu.mobile.main.Data.Models

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserDto(
    @SerialName("userId")
    val id: Int,
    val login: String,
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String?,
    @SerialName("roleId")
    val roleId: Int,
    @SerialName("authAllowed")
    val authAllowed: Boolean,
    @SerialName("personId")
    val personId: Int,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("lastLoginDate")
    val lastLoginDate: String?   // может быть null
)