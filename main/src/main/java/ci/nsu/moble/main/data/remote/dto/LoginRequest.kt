// Task_6: Запрос на логин — POST /auth/login.
package ci.nsu.moble.main.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String
)
