package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.calculation.CalculationScreen
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.main.MainViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenList.Main.route
    ) {
        composable(ScreenList.Main.route) {
            val viewModel: MainViewModel = viewModel()
            MainScreen(
                viewModel = viewModel,
                onNavigateToCalculation = {
                    navController.navigate(ScreenList.Calculation.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToHistory = {
                    navController.navigate(ScreenList.History.route){
                        launchSingleTop = true
                    }
                }
            )


        }

        composable(ScreenList.Calculation.route) {
            CalculationScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToAdditional = {
                    navController.navigate(ScreenList.Additional.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(ScreenList.Additional.route) {
            // Здесь будет AdditionalScreen
        }

        composable(ScreenList.History.route) {
            // Здесь будет HistoryScreen
        }
    }
}