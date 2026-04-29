package ci.nsu.mobile.main.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val groupId: Int,
    val groupName: String
)