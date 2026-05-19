package ci.nsu.mobile.main.Data.Models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String,
    val person: PersonDto
)