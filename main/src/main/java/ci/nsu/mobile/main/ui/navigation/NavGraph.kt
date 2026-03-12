package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.main.MainViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            val viewModel: MainViewModel = viewModel()
            MainScreen(
                viewModel = viewModel,
                onNavigateToCalculation = {
                    navController.navigate(Screen.Calculation.route)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        composable(Screen.Calculation.route) {
            // Здесь будет CalculationScreen
        }

        composable(Screen.Additional.route) {
            // Здесь будет AdditionalScreen
        }

        composable(Screen.History.route) {
            // Здесь будет HistoryScreen
        }
    }
}