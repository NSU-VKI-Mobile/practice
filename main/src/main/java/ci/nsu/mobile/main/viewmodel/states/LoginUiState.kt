package ci.nsu.mobile.main.viewmodel.states

data class LoginUiState(
    val login: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)