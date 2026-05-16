package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.RegistrationScreen
import ci.nsu.mobile.main.ui.screens.UsersScreen
import ci.nsu.mobile.main.viewmodel.LoginViewModel
import ci.nsu.mobile.main.viewmodel.RegistrationViewModel

sealed class Screens(val route: String) {
    object LoginScreen: Screens("LoginScreen")
    object RegistrationScreen: Screens("RegistrationScreen")
    object UsersScreen: Screens("UsersScreen")
}

@Composable
fun Navigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegistrationViewModel,
) {
    NavHost(navController, startDestination = Screens.LoginScreen.route) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screens.UsersScreen.route) {
                    popUpTo(Screens.LoginScreen.route) { inclusive = true }
                }},
                navTo = { navigateTo -> navController.navigate(navigateTo) },
                viewModel = loginViewModel
            )
        }
        composable(Screens.RegistrationScreen.route) {
            RegistrationScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screens.UsersScreen.route) {
                        popUpTo(Screens.RegistrationScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screens.UsersScreen.route) {
            UsersScreen(
                navTo = { navigateTo ->
                    navController.navigate(navigateTo) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}