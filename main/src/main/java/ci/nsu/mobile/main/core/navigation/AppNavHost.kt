package ci.nsu.mobile.main.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.features.auth.login.LoginRoute
import ci.nsu.mobile.main.features.auth.register.RegisterRoute
import ci.nsu.mobile.main.features.main.MainRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginRoute(navController)
        }
        composable(Screen.Register.route) {
            RegisterRoute(navController)
        }
        composable(Screen.Main.route) {
            MainRoute(navController)
        }
    }
}