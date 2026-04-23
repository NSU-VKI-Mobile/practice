package ci.nsu.mobile.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("roleId")
    val roleId: Int = 1,              // По заданию всегда 1, значение по умолчанию упрощает создание
    @SerialName("authAllowed")
    val authAllowed: Boolean = true,  // Аналогично
    val person: PersonDto             // Вложенная структура, сериализуется рекурсивно
)