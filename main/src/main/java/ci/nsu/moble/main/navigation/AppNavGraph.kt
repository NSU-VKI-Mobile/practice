package ci.nsu.moble.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel  // ← добавь этот импорт
import ci.nsu.moble.main.presentation.*
import ci.nsu.moble.main.presentation.viewmodel.CalculationViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    // 👇 Создаём ViewModel ОДИН РАЗ для всего графа
    val calculationViewModel: CalculationViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController)
        }
        composable("step1") {
            Step1Screen(navController, calculationViewModel)
        }
        composable("step2") {
            Step2Screen(navController, calculationViewModel)
        }
        composable("result") {
            ResultScreen(navController, calculationViewModel)
        }
        composable("history") {
            HistoryScreen(navController)
        }
    }
}