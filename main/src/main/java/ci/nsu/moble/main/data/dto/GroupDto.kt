package ci.nsu.moble.main.data.dto

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("groupId")
    val id: Int,

    @SerializedName("groupName")
    val name: String
)