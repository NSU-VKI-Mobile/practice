package ci.nsu.mobile.main.data.models

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("groupId")
    val groupId: Int,
    @SerializedName("groupName")
    val groupName: String
)