package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.screens.*

@Composable
fun AppNavigation(viewModel: DepositViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController, viewModel) }
        composable("step1") { Step1Screen(navController, viewModel) }
        composable("step2") { Step2Screen(navController, viewModel) }
        composable("result") { ResultScreen(navController, viewModel) }
        composable("history") { HistoryScreen(navController, viewModel) }
    }
}