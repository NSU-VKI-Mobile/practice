package ci.nsu.mobile.auth.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val groupId: Int,
    val groupName: String
)