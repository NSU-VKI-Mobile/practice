
@Serializable
data class GroupDto(
    @SerialName("groupId") val id: Int,
    @SerialName("groupName") val name: String
)
