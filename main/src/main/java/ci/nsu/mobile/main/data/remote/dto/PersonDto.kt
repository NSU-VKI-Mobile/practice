package ci.nsu.mobile.main.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String, // "YYYY-MM-DD"
    val gender: String,    // "MALE" или "FEMALE"
    val groupId: Int
)