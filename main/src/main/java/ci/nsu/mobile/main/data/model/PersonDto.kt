package ci.nsu.mobile.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val birthDate: String,
    val gender: String,
    val groupId: Int
)