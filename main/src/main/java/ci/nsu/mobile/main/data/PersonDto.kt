package ci.nsu.mobile.main.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("middleName")
    val middleName: String? = null,
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("groupId")
    val groupId: Int
)