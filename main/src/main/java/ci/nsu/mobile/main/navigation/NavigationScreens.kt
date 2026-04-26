package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.RegistrationScreen
import ci.nsu.mobile.main.ui.screens.UsersScreen

sealed class Screens(val route: String) {
    object LoginScreen: Screens("LoginScreen")
    object RegistrationScreen: Screens("RegistrationScreen")
    object UsersScreen: Screens("UsersScreen")
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screens.LoginScreen.route) {
        composable(Screens.LoginScreen.route) { LoginScreen { navigateTo -> navController.navigate(navigateTo)} }
        composable(Screens.RegistrationScreen.route) { RegistrationScreen { navigateTo -> navController.navigate(navigateTo)} }
        composable(Screens.UsersScreen.route) { UsersScreen { navigateTo -> navController.navigate(navigateTo)} }
    }
}