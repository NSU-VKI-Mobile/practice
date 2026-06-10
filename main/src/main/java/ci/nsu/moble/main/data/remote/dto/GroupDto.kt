// Task_6: DTO для группы — получаем из GET /groups.
package ci.nsu.moble.main.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId") val id: Int,
    @SerialName("groupName") val name: String
)
