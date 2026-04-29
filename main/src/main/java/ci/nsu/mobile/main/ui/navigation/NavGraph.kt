package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.ui.auth.login.LoginScreen
import ci.nsu.mobile.main.ui.auth.login.LoginViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
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
        // TODO: Add users screen
    }
}