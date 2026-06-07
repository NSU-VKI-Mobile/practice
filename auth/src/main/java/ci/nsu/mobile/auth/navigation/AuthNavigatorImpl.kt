package ci.nsu.mobile.auth.navigation

import androidx.navigation.NavHostController
import ci.nsu.mobile.domain.interfaces.AuthNavigator
import ci.nsu.mobile.domain.navigation.Screens
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthNavigatorImpl @Inject constructor() : AuthNavigator {

    override fun navigateToLogin(navController: NavHostController) {
        navController.navigate(Screens.LoginScreen.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    override fun navigateToRegister(navController: NavHostController) {
        navController.navigate(Screens.RegistrationScreen.route) {
            launchSingleTop = true
        }
    }

    override fun openAuthFlow(navController: NavHostController) {
        navigateToLogin(navController)
    }
}