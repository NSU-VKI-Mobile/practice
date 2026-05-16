package ci.nsu.mobile.main.auth.ui.users

import ci.nsu.mobile.main.auth.data.dto.UserDto

sealed class UsersState {
    object Idle : UsersState()
    object Loading : UsersState()
    data class Success(val users: List<UserDto>) : UsersState()
    data class Error(val message: String) : UsersState()
}

data class UsersUiState(
    val usersState: UsersState = UsersState.Idle,
    val currentUserId: Long? = null
)