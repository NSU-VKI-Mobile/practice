package ci.nsu.moble.main.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Interprets JSON (Serializable) as individual objects
@Parcelize // so it can be saved in saved state
@Serializable // json
data class GroupDto(
    @SerialName("groupId") val id: Int,
    @SerialName("groupName") val name: String
) : Parcelable