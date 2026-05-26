package ci.nsu.mobile.main.Data.DataModels

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val groupId: Int,
    val groupName: String
)