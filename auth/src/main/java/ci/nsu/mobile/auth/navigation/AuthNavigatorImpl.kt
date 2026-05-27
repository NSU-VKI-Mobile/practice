package ci.nsu.mobile.auth.navigation

import androidx.navigation.NavController
import ci.nsu.mobile.domain.auth.AuthNavigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthNavigatorImpl @Inject constructor() : AuthNavigator {

    private var navController: NavController? = null
    private var resultCallback: ((Int) -> Unit)? = null

    fun setNavController(controller: NavController) {
        navController = controller
    }

    // Для startActivityForResult в Compose
    fun setResultCallback(callback: (Int) -> Unit) {
        resultCallback = callback
    }

    override fun navigateToLogin() {
        navController?.navigate("login") {
            popUpTo(0) { inclusive = true }
        }
    }

    override fun navigateToRegister() {
        navController?.navigate("register")
    }

    override fun openAuthFlow(requestCode: Int) {
        // В Compose это можно реализовать через result callback
        navController?.navigate("login")
        // При возврате можно вызвать resultCallback
    }

    override fun navigateToUsers() {
        navController?.navigate("users")
    }
}