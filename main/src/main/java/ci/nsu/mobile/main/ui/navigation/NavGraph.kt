package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.ui.auth.login.LoginScreen
import ci.nsu.mobile.main.ui.auth.login.LoginViewModel
import ci.nsu.mobile.main.ui.users.UsersScreen
import ci.nsu.mobile.main.ui.users.UsersViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    usersViewModel: UsersViewModel,
    startDestination: String = Route.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(Route.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Route.Register.route)
                },
                onNavigateToUsers = { token ->
                    // Save token if needed, then navigate
                    navController.navigate(Route.Users.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }

        // TODO: Add register screen

        composable(Route.Users.route) {
            UsersScreen(
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = usersViewModel
            )
        }
    }
}