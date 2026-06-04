package ci.nsu.moble.main.viewmodel.states

import ci.nsu.moble.main.data.dto.GroupDto

// Groups states for loading groups from server
sealed class GroupsState {
    object Idle : GroupsState()
    object Loading : GroupsState()
    data class Success(val data: List<GroupDto>) : GroupsState()
    data class Error(val message: String) : GroupsState()
}
