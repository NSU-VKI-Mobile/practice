package ci.nsu.mobile.auth.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
    val groupId: Int
)
