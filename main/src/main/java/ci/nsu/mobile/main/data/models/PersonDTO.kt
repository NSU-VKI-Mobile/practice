package ci.nsu.mobile.main.data.models

import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("middleName") val middleName: String?,
    @SerializedName("birthDate") val birthDate: String,   // формат: "yyyy-MM-dd"
    @SerializedName("gender") val gender: String,         // "MALE" / "FEMALE"
    @SerializedName("groupId") val groupId: Int
)
