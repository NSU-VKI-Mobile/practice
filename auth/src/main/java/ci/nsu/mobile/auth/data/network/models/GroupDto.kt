package ci.nsu.mobile.auth.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val groupId: Int,
    val groupName: String
)