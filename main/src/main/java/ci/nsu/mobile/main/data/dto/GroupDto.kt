package ci.nsu.mobile.main.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val id: Int,
    val name: String
)