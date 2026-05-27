package ci.nsu.mobile.domain.auth

interface AuthNavigator {
    fun navigateToLogin()
    fun navigateToRegister()
    fun openAuthFlow(requestCode: Int)
    fun navigateToUsers()
}