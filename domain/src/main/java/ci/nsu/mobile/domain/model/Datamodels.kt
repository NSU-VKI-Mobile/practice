package ci.nsu.mobile.domain.model

import com.google.gson.annotations.SerializedName

// --- АВТОРИЗАЦИЯ И ПОЛЬЗОВАТЕЛИ ---

data class LoginRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

data class PersonDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("middleName") val middleName: String? = null,
    @SerializedName("birthDate") val birthDate: String,
    @SerializedName("gender") val gender: String,
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

data class LoginResponse(
    @SerializedName("token") val token: String
)

data class UserDto(
    @SerializedName("userId") val userId: Long,
    @SerializedName("login") val login: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("roleId") val roleId: Int? = null,
    @SerializedName("personId") val personId: Long? = null
)

data class GroupDto(
    @SerializedName("groupId") val groupId: Int,
    @SerializedName("groupName") val groupName: String
)

// --- РАСЧЕТЫ (Чистая модель для передачи между модулями) ---
// В Room будет своя сущность, а здесь - просто данные
data class DepositCalculation(
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double = 0.0,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
)