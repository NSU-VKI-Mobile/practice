package ci.nsu.moble.main.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String,
    val gender: String,
    val groupId: Int
)