package ci.nsu.mobile.main.data.models

import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("middleName")
    val middleName: String,
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("groupId")
    val groupId: Int
)