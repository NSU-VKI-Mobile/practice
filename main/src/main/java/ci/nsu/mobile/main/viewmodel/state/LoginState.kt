package ci.nsu.mobile.main.viewmodel.state

data class LoginState (
    val login: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

sealed class LoginEvents {
    data class LoginChanged(val newLogin: String): LoginEvents()
    data class PasswordChanged(val newPassword: String): LoginEvents()
    object SubmitLogin: LoginEvents()
    object ValidationScreen: LoginEvents()
    object CleanAll: LoginEvents()
}