package ci.nsu.mobile.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val birthDate: String,
    val gender: String,
    val groupId: Int
)