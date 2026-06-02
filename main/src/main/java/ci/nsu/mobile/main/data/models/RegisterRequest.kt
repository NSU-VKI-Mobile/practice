package ci.nsu.mobile.main.data.models

//запрос на регистрацию
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1, // Всегда 1, значение по умолчанию
    val authAllowed: Boolean = true, //разрешает аутентификацию
    val person: PersonDto
)