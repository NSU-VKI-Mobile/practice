package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ci.nsu.mobile.main.ui.*
import ci.nsu.mobile.main.ui.theme.LoginScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                navController = navController
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController
            )
        }

        composable("main") {
            MainScreen(
                navController = navController
            )
        }
    }
}