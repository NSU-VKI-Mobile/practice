package ci.nsu.mobile.main.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val surname: String,
    val name: String,
    val patronymic: String,
    val birthday: String,
    val gender: String,
    val groupId: Int
)
