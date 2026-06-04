package ci.nsu.mobile.main.viewmodel.states

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)