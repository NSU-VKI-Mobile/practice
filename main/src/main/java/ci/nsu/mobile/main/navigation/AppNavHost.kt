package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ci.nsu.mobile.main.ui.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("step1") { Step1Screen(navController) }
        composable("step2") { Step2Screen(navController) }
        composable("result") { ResultScreen(navController) }
        composable("history") { HistoryScreen(navController) }
    }
}