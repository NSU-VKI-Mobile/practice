package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.ui.auth.login.LoginScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    // TODO: Navigate to register screen
                },
                onNavigateToUsers = {
                    // TODO: Navigate to users screen
                }
            )
        }

        // TODO: Add register screen
        // TODO: Add users screen
    }
}