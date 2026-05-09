package ci.nsu.moble.main.data.dto

import kotlinx.serialization.Serializable

// Personal info of user (not login)
@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,// not everyone has it
    val birthDate: String,  // "YYYY-MM-DD"
    val gender: String,     // "MALE" / "FEMALE"
    val groupId: Int
)
