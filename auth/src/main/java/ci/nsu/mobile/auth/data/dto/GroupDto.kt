@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package ci.nsu.mobile.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId") val id: Int,
    @SerialName("groupName") val name: String
)