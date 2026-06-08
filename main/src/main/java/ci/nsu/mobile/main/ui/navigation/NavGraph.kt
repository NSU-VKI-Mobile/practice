package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.screens.Step1Screen
import ci.nsu.mobile.main.ui.screens.Step2Screen
import ci.nsu.mobile.main.viewModel.DepositViewModel

@Composable
fun NavGraph(viewModel: DepositViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.Main.route) {

        composable(Screen.Main.route) {
            MainScreen(navController)
        }

        composable(Screen.Step1.route) {
            Step1Screen(navController, viewModel)
        }

        composable(Screen.Step2.route) {
            Step2Screen(navController, viewModel)
        }

        composable(Screen.Result.route) {
            ResultScreen(navController, viewModel)
        }

        composable(Screen.History.route) {
            HistoryScreen(navController, viewModel)
        }

    }
}