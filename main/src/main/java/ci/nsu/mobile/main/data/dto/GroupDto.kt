package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class GroupDto(
    @JsonNames("groupId")
    val id: Int,
    @JsonNames("groupName")
    val name: String
)
