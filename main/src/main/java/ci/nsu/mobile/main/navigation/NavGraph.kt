package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import ci.nsu.mobile.main.ui.history.HistoryScreen
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.result.ResultScreen
import ci.nsu.mobile.main.ui.step1.Step1Screen
import ci.nsu.mobile.main.ui.step2.Step2Screen
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    val viewModel: DepositViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        composable("main") {
            MainScreen(navController)
        }

        composable("step1") {
            Step1Screen(navController, viewModel)
        }

        composable("step2") {
            Step2Screen(navController, viewModel)
        }

        composable("result") {
            ResultScreen(navController, viewModel)
        }

        composable("history") {
            HistoryScreen(navController, viewModel)
        }
    }
}