package ci.nsu.mobile.main.ui.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModelFactory

private object Routes {
    const val Main = "main_screen"
    const val Step1 = "step_1"
    const val Step2 = "step_2"
    const val Result = "result_screen"
    const val History = "history_screen"
    const val HistoryDetail = "history_detail/{calculationId}"

    fun historyDetail(calculationId: Long): String {
        return "history_detail/$calculationId"
    }
}

@Composable
fun DepApp(onExitClick: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val depositViewModel: DepositViewModel = viewModel(
        factory = DepositViewModelFactory(context)
    )
    val state by depositViewModel.uiState.collectAsState()
    val history by depositViewModel.history.collectAsState()

    fun navigateHome() {
        depositViewModel.clearDraft()
        navController.popBackStack(Routes.Main, inclusive = false)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Main
    ) {
        composable(Routes.Main) {
            MainScreen(
                onCalculateClick = {
                    depositViewModel.clearDraft()
                    navController.navigate(Routes.Step1)
                },
                onHistoryClick = { navController.navigate(Routes.History) },
                onExitClick = onExitClick
            )
        }

        composable(Routes.Step1) {
            Step1Screen(
                state = state,
                onAmountChange = depositViewModel::updateInitialAmount,
                onMonthsChange = depositViewModel::updatePeriodMonths,
                onHomeClick = ::navigateHome,
                onNextClick = {
                    if (depositViewModel.validateStep1()) {
                        navController.navigate(Routes.Step2)
                    }
                }
            )
        }

        composable(Routes.Step2) {
            Step2Screen(
                state = state,
                onRateSelected = depositViewModel::updateSelectedRate,
                onTopUpChange = depositViewModel::updateMonthlyTopUp,
                onBackClick = { navController.popBackStack() },
                onCalculateClick = {
                    if (depositViewModel.calculateResult()) {
                        navController.navigate(Routes.Result)
                    }
                }
            )
        }

        composable(Routes.Result) {
            ResultScreen(
                result = state.result,
                isSaved = state.isSaved,
                saveMessage = state.saveMessage,
                onSaveClick = depositViewModel::saveCurrentResult,
                onHomeClick = ::navigateHome
            )
        }

        composable(Routes.History) {
            HistoryScreen(
                history = history,
                onBackClick = { navController.popBackStack() },
                onItemClick = { calculationId ->
                    navController.navigate(Routes.historyDetail(calculationId))
                }
            )
        }

        composable(
            route = Routes.HistoryDetail,
            arguments = listOf(
                navArgument("calculationId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val calculationId = backStackEntry.arguments?.getLong("calculationId") ?: 0L
            val calculationFlow = remember(calculationId) {
                depositViewModel.getCalculationById(calculationId)
            }
            val calculation by calculationFlow.collectAsState(initial = null)

            HistoryDetailScreen(
                calculation = calculation,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}