package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repositories.DepositRepository
import ci.nsu.mobile.main.ui.screens.HomeScreen
import ci.nsu.mobile.main.ui.screens.DepositInputScreen
import ci.nsu.mobile.main.ui.screens.AdditionalParamsScreen
import ci.nsu.mobile.main.ui.screens.HistoryDetailScreen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.viewmodel.HistoryViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    DepositApp(onExit = this::finish)
                }
            }
        }
    }
}

@Composable
fun DepositApp(onExit: () -> Unit) {
    val navController = rememberNavController()

    val database = AppDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current.applicationContext)
    val repository = DepositRepository(database)

    val viewModel: DepositViewModel = viewModel(
        factory = viewModelFactory {
            initializer { DepositViewModel(repository) }
        }
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = viewModelFactory {
            initializer { HistoryViewModel(repository) }
        }
    )

    val calculations by historyViewModel.calculations.collectAsState()
    val selectedCalculation by historyViewModel.selectedCalculation.collectAsState()
    val isLoading by historyViewModel.isLoading.collectAsState()
    val error by historyViewModel.error.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Главный экран
        composable("home") {
            HomeScreen(
                onCalculateClick = {
                    navController.navigate("deposit_input")
                },
                onHistoryClick = {
                    navController.navigate("history")
                },
                onExitClick = onExit
            )
        }

        // Экран ввода параметров
        composable("deposit_input") {
            DepositInputScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = { amount, months ->
                    viewModel.saveFirstScreenData(amount, months)
                    navController.navigate("additional_params")
                }
            )
        }

        // Экран для дополнительных параметров
        composable("additional_params") {
            AdditionalParamsScreen(
                periodMonths = viewModel.getPeriodMonths(),
                onBackClick = {
                    navController.popBackStack()
                },
                onCalculateClick = { rate, topUp ->
                    viewModel.saveSecondScreenData(rate, topUp)
                    navController.navigate("result")
                }
            )
        }

        // Экран результата
        composable("result") {
            ResultScreen(
                initialAmount = viewModel.getInitialAmount(),
                periodMonths = viewModel.getPeriodMonths(),
                interestRate = viewModel.getInterestRate(),
                monthlyTopUp = viewModel.getMonthlyTopUp(),
                finalAmount = viewModel.getFinalAmount(),
                interestEarned = viewModel.getInterestEarned(),
                onSaveClick = {
                    viewModel.saveCalculation()
                    navController.popBackStack("home", inclusive = false)
                },
                onBackToHomeClick = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        // Экран истории
        composable("history") {
            HistoryScreen(
                calculations = calculations,
                isLoading = isLoading,
                error = error,
                onItemClick = { id ->
                    historyViewModel.selectCalculation(id)
                    navController.navigate("history_detail")
                },
                onLoad = {
                    historyViewModel.loadCalculations()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Экран деталей
        composable("history_detail") {
            HistoryDetailScreen(
                calculation = selectedCalculation,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

    }
}