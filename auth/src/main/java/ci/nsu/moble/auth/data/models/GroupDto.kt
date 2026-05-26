package ci.nsu.moble.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("groupName")
    val groupName: String
)