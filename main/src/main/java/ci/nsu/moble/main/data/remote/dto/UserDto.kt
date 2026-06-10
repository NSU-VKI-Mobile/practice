// Task_6: DTO пользователя (ответ логина / список пользователей).
package ci.nsu.moble.main.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("login") val login: String = "",
    @SerialName("token") val token: String? = null,
    @SerialName("firstName") val firstName: String? = null,
    @SerialName("lastName") val lastName: String? = null,
    @SerialName("email") val email: String? = null
)
