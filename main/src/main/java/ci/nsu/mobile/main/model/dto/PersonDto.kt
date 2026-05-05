package ci.nsu.mobile.main.model.dto

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String,
    val gender: String,
    val groupId: Int,
)