package ci.nsu.mobile.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String, // Ожидаемый формат сервером, например "yyyy-MM-dd"
    val gender: String,
    val groupId: Int
)

