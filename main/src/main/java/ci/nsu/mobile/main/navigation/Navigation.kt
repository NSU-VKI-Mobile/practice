package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.ui.depositScreens.FirstScreenContent
import ci.nsu.mobile.main.ui.depositScreens.HistoryScreenContent
import ci.nsu.mobile.main.ui.depositScreens.MainScreenContent
import ci.nsu.mobile.main.ui.depositScreens.ResultScreenContent
import ci.nsu.mobile.main.ui.depositScreens.SecondScreenContent
import ci.nsu.mobile.main.viewmodel.deposit.DepositCalculationViewModel
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryDepositsViewModel

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