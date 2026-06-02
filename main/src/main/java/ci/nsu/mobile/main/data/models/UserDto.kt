package ci.nsu.mobile.main.data.models

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId") //Сервер возвращает "userId"
    val id: Int,
    val login: String,
    val email: String,
    val phoneNumber: String?,
    val roleId: Int,
    val authAllowed: Boolean,
    val person: PersonDto?
)