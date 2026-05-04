package ci.nsu.mobile.main.viewmodel.state

data class LoginState (
    val login: String = "",
    val password: String = "",
    val result: Boolean = false
)