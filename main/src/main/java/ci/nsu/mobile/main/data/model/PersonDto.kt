package ci.nsu.mobile.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,          // Отчество может отсутствовать
    val birthDate: String,            // Пока строка; преобразование в LocalDate оставим для ViewModel
    val gender: String,               // Зависит от сервера: "MALE"/"FEMALE" или "М"/"Ж"
    val groupId: Int
)