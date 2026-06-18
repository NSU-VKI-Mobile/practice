package ci.nsu.mobile.main.Data.Models

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GroupDto(
    @SerialName("groupId")
    val id: Int,
    @SerialName("groupName")
    val name: String
)