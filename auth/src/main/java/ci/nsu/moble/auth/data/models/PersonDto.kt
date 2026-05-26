package ci.nsu.moble.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("middleName")
    val middleName: String = "",
    @SerialName("birthDate")
    val birthDate: String = "",
    @SerialName("gender")
    val gender: String = "",
    @SerialName("groupId")
    val groupId: Int = 0
)