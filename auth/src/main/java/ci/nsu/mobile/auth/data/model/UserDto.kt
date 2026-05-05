package ci.nsu.mobile.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int? = null,
    val login: String,
    val email: String? = null,
    val token: String? = null // Сюда мы будем парсить токен, если бэкенд отдает его прямо в теле ответа
)