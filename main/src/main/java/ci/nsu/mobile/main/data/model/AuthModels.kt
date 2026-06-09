package ci.nsu.mobile.main.data.model

import com.google.gson.annotations.SerializedName

// --- ЗАПРОСЫ ---

data class LoginRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

data class PersonDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("middleName") val middleName: String? = null,
    @SerializedName("birthDate") val birthDate: String, // "YYYY-MM-DD"
    @SerializedName("gender") val gender: String,      // "MALE" или "FEMALE"
    @SerializedName("groupId") val groupId: Int
)

data class RegisterRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("roleId") val roleId: Int = 1,
    @SerializedName("authAllowed") val authAllowed: Boolean = true,
    @SerializedName("person") val person: PersonDto
)

// --- ОТВЕТЫ ---

data class LoginResponse(
    @SerializedName("token") val token: String
)

// Пользователь для списка (GET /users)
data class UserDto(
    @SerializedName("userId") val userId: Long,
    @SerializedName("login") val login: String,
    @SerializedName("email") val email: String? = null, // 🟢 ИСПРАВЛЕНО: было email? String?, стало email: String?
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("roleId") val roleId: Int? = null,
    @SerializedName("personId") val personId: Long? = null
)

// Группа для выпадающего списка (GET /groups)
data class GroupDto(
    @SerializedName("groupId") val groupId: Int,
    @SerializedName("groupName") val groupName: String
)