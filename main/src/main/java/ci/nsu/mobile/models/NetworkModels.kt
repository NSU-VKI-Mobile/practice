package ci.nsu.mobile.models

import com.google.gson.annotations.SerializedName

// Моделька группы. Сервер шлёт нам JSON с именами "groupId" и "groupName",
// а мы с помощью аннотаций превращаем их в удобные id и name
data class GroupDto(
    @SerializedName("groupId")
    val id: Int,
    @SerializedName("groupName")
    val name: String
)

// Данные студента, которые лежат внутри большого запроса на регистрацию
// Сюда улетает наша перевёрнутая дата (ГГГГ-ММ-ДД) и числовой ID группы
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?, // Отчество может быть null, если его не ввели
    val birthDate: String,
    val gender: String,
    val groupId: Int
)

// Тот самый большой пакет данных для регистрации, который требует бэкенд нгу
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

// Моделька пользователя, которую нам возвращает сервер после успешного логина
// Самое ценное тут это токен, его мы бережно сохраняем
data class UserDto(
    val id: Int,
    val login: String,
    val token: String? = null
)

// Простой запрос на авторизацию только логин и пароль, ничего лишнего
data class LoginRequest(
    val login: String,
    val password: String
)
