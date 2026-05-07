package ci.nsu.mobile.main.units.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
data class GroupDto(
    @JsonNames("groupId", "id")
    val id: Int,
    @JsonNames("groupName", "name")
    val name: String
)
