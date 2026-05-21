package ci.nsu.mobile.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId")
    val id: Int,
    @SerialName("groupName")
    val name: String
)