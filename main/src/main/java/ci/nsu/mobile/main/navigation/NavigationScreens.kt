package ci.nsu.mobile.main.navigation

import UsersScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.ui.authScreens.LoginScreen
import ci.nsu.mobile.main.ui.authScreens.RegistrationScreen
import ci.nsu.mobile.main.viewmodel.login.LoginViewModel
import ci.nsu.mobile.main.viewmodel.registration.RegistrationViewModel
import ci.nsu.mobile.main.viewmodel.users.UsersViewModel

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
    usersViewModel: UsersViewModel
) {
    NavHost(navController, startDestination = Screens.LoginScreen.route) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screens.UsersScreen.route) },
                navTo = { navigateTo -> navController.navigate(navigateTo) },
                viewModel = loginViewModel
            )
        }
        composable(Screens.RegistrationScreen.route) {
            RegistrationScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = { navController.navigate(Screens.LoginScreen.route) }
            )
        }
        composable(Screens.UsersScreen.route) {
            UsersScreen(
                navTo = { navigateTo ->  navController.navigate(navigateTo)},
                viewModel = usersViewModel
            )
        }
    }
}