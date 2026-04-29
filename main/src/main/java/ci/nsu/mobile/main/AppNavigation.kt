package ci.nsu.mobile.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screen.login.LoginScreen
import ci.nsu.mobile.main.ui.screen.main.MainScreen
import ci.nsu.mobile.main.ui.screen.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main") { popUpTo("login") { inclusive = true } } },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("login") { popUpTo("register") { inclusive = true } } },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("main") {
            MainScreen(
                onLogout = { navController.navigate("login") { popUpTo("main") { inclusive = true } } }
            )
        }
    }
}
