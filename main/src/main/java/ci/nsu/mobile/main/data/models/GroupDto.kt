package ci.nsu.mobile.main.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val groupId: Int,
    val groupName: String
)