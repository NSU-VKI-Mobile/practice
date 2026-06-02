package ci.nsu.mobile.main.Data.Models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val birthDate: String, // формат "yyyy-MM-dd"
    val gender: String,    // "male", "female"
    val groupId: Int
)