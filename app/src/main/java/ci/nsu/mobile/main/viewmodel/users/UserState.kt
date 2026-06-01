package ci.nsu.mobile.main.viewmodel.users

import ci.nsu.mobile.main.data.network.model.UserDto

data class UserState(
    val users: List<UserDto> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val selectedBottomItem: String = "users"
)

sealed class UserEvents {
    data class BottomItemChanged(val newItem: String) : UserEvents()
}