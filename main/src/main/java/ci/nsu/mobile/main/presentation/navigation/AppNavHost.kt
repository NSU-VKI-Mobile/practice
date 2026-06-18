package ci.nsu.mobile.main.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.presentation.screens.MainScreen
import ci.nsu.mobile.main.presentation.screens.MainViewModel
import ci.nsu.mobile.main.presentation.screens.login.LoginScreen
import ci.nsu.mobile.main.presentation.screens.register.RegisterScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = (context.applicationContext as DepositApplication).locator.tokenManager

    val startDest = if (tokenManager.isLoggedIn()) "main" else "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("main") {
            val mainViewModel: MainViewModel = viewModel()
            MainScreen(viewModel = mainViewModel)
        }
    }
}