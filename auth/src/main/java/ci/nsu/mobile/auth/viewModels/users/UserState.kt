package ci.nsu.mobile.auth.viewModels.users

import ci.nsu.mobile.domain.models.User

data class UserState(
    val users: List<User> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val selectedBottomItem: String = "users"
)

sealed class UserEvents {
    data class BottomItemChanged(val newItem: String) : UserEvents()
}