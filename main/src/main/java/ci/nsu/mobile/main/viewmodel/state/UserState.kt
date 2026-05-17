package ci.nsu.mobile.main.viewmodel.state

import ci.nsu.mobile.main.data.network.model.UserDto

data class UserState(
    val users: List<UserDto> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)