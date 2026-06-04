package ci.nsu.mobile.main.model

import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("middleName")
    val middleName: String? = null,
    @SerializedName("birthDate")
    val birthDate: String,
    val gender: String,
    @SerializedName("groupId")
    val groupId: Int
)

