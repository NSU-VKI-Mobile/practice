package ci.nsu.mobile.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDto(

    @SerializedName("userId")
    val userId: Long,

    val login: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int,
    val authAllowed: Boolean,
    val personId: Long,
    val createdDate: String,
    val lastLoginDate: String?

) : Parcelable