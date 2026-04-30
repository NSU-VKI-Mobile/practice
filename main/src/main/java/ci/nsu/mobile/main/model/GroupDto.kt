package ci.nsu.mobile.main.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val groupId: Int,
    val groupName: String
)