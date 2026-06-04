package ci.nsu.mobile.auth.di

import androidx.navigation.NavController
import ci.nsu.mobile.domain.navigation.AuthNavigator

class AuthNavigatorImpl(
    private val navController: NavController
) : AuthNavigator {
    override fun navigateToLogin() {
        navController.navigate("login")
    }

    override fun navigateToRegister() {
        navController.navigate("register")
    }
}