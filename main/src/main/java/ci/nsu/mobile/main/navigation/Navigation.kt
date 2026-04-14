package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.presentation.ui.screens.FirstScreenContent
import ci.nsu.mobile.main.presentation.ui.screens.HistoryScreenContent
import ci.nsu.mobile.main.presentation.ui.screens.MainScreenContent
import ci.nsu.mobile.main.presentation.ui.screens.ResultScreenContent
import ci.nsu.mobile.main.presentation.ui.screens.SecondScreenContent
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.HistoryDepositsViewModel

sealed class Screen(val route: String) {
    object MainScreen : Screen("MainScreen")
    object FirstScreen : Screen("FirstScreen")
    object SecondScreen : Screen("SecondScreen")
    object ResultScreen: Screen("ResultScreen")
    object HistoryScreen: Screen("HistoryScreen")
}

@Composable
fun NavControlFun(navController: NavHostController,
                  depositCalculationViewModel: DepositCalculationViewModel,
                  historyDepositsViewModel: HistoryDepositsViewModel) {
    NavHost(navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {
            MainScreenContent { navigateTo -> navController.navigate(navigateTo) }
        }
        composable(Screen.FirstScreen.route) {
            FirstScreenContent({navigateTo -> navController.navigate(navigateTo)}, depositCalculationViewModel)
        }
        composable(Screen.SecondScreen.route) {
            SecondScreenContent({navigateTo -> navController.navigate(navigateTo)}, depositCalculationViewModel)
        }
        composable(Screen.HistoryScreen.route) {
            HistoryScreenContent({navigateTo -> navController.navigate(navigateTo)}, historyDepositsViewModel)
        }
        composable(Screen.ResultScreen.route) {
            ResultScreenContent({navigateTo -> navController.navigate(navigateTo)}, depositCalculationViewModel)
        }
    }
}