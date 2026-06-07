package ci.nsu.mobile.main.viewmodel.users

import ci.nsu.mobile.main.data.network.model.User

data class UserState(
    val users: List<User> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val selectedBottomItem: String = "users"
)

sealed class UserEvents {
    data class BottomItemChanged(val newItem: String) : UserEvents()
}