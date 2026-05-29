package ci.nsu.moble.main.viewmodel.states

import ci.nsu.moble.main.data.dto.GroupDto

data class RegisterUiState(
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phone: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String = "",
    val gender: String = "MALE",
    val selectedGroup: GroupDto? = null,

    val isLoading: Boolean = false,
    val error: String? = null,

    val groupsState: GroupsState = GroupsState.Idle
)