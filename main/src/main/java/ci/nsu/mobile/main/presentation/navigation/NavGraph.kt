package ci.nsu.mobile.main.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.presentation.screens.home.HomeScreen
import ci.nsu.mobile.main.presentation.screens.login.LoginScreen
import ci.nsu.mobile.main.presentation.screens.register.RegisterScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = (context.applicationContext as DepositApplication).tokenManager

    // Проверка авторизации при старте
    LaunchedEffect(Unit) {
        if (tokenManager.isLoggedIn()) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable("home") {
            HomeScreen(navController = navController)
        }
    }
}