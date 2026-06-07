package ci.nsu.mobile.calculations.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ci.nsu.mobile.calculations.ui.screens.FirstScreenContent
import ci.nsu.mobile.calculations.ui.screens.HistoryScreenContent
import ci.nsu.mobile.calculations.ui.screens.MainScreenContent
import ci.nsu.mobile.calculations.ui.screens.ResultScreenContent
import ci.nsu.mobile.calculations.ui.screens.SecondScreenContent
import ci.nsu.mobile.calculations.viewModels.deposit.DepositCalculationViewModel
import ci.nsu.mobile.calculations.viewModels.historyDeposits.HistoryDepositsViewModel
import ci.nsu.mobile.domain.navigation.Screens

fun NavGraphBuilder.calculationsNavGraph(
    navController: NavHostController,
    historyDepositsViewModel: HistoryDepositsViewModel,
    depositCalculationViewModel: DepositCalculationViewModel
) {
    composable(Screens.HistoryScreen.route) {
        HistoryScreenContent(
            navToScreen = { navigateTo -> navController.navigate(navigateTo) },
            viewModel = historyDepositsViewModel
        )
    }

    composable(Screens.MainScreen.route) {
        MainScreenContent(
            navToScreen = { navigateTo -> navController.navigate(navigateTo) },
            viewModel = depositCalculationViewModel
        )
    }

    composable(Screens.FirstScreen.route) {
        FirstScreenContent(
            navToScreen = { navigateTo -> navController.navigate(navigateTo) },
            viewModel = depositCalculationViewModel
        )
    }

    composable(Screens.SecondScreen.route) {
        SecondScreenContent(
            navToScreen = { navigateTo -> navController.navigate(navigateTo) },
            viewModel = depositCalculationViewModel
        )
    }

    composable(Screens.ResultScreen.route) {
        ResultScreenContent(
            navToScreen = { navigateTo -> navController.navigate(navigateTo) },
            viewModel = depositCalculationViewModel
        )
    }
}