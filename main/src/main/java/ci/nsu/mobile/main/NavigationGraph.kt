package ci.nsu.mobile.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object InputFirst : Screen("input_first")
    object InputSecond : Screen("input_second")
    object Result : Screen("result")
    object History : Screen("history")
    object HistoryDetail : Screen("history_detail/{calculationId}") {
        fun passId(id: Long): String {
            Log.d("Screen", "passId вызывается с id: $id")
            return "history_detail/$id"
        }
    }
}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToInput = { navController.navigate(Screen.InputFirst.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }

        composable(Screen.InputFirst.route) {
            InputFirstScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSecond = { amount, period ->
                    navController.navigate("input_second?amountStr=${amount}&periodStr=${period}")
                }
            )
        }

        composable("input_second?amountStr={amountStr}&periodStr={periodStr}") { backStackEntry ->
            val amountStr = backStackEntry.arguments?.getString("amountStr") ?: "0"
            val periodStr = backStackEntry.arguments?.getString("periodStr") ?: "0"

            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val period = periodStr.toIntOrNull() ?: 0

            InputSecondScreen(
                initialAmount = amount,
                periodMonths = period,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { topUp, rate ->
                    navController.navigate("result?amountStr=${amount}&periodStr=${period}&rateStr=${rate}&topUpStr=${topUp}")
                }
            )
        }

        composable(
            route = "result?amountStr={amountStr}&periodStr={periodStr}&rateStr={rateStr}&topUpStr={topUpStr}"
        ) { backStackEntry ->
            val amountStr = backStackEntry.arguments?.getString("amountStr") ?: "0"
            val periodStr = backStackEntry.arguments?.getString("periodStr") ?: "0"
            val rateStr = backStackEntry.arguments?.getString("rateStr") ?: "0"
            val topUpStr = backStackEntry.arguments?.getString("topUpStr") ?: "0"

            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val period = periodStr.toIntOrNull() ?: 0
            val rate = rateStr.toDoubleOrNull() ?: 0.0
            val topUp = topUpStr.toDoubleOrNull() ?: 0.0

            // Создаём ViewModel
            val viewModel: ResultViewModel = viewModel()

            // Рассчитываем итоговую сумму и проценты
            val (finalAmount, interestEarned) = calculateDeposit(
                initialAmount = amount,
                periodMonths = period,
                annualRate = rate,
                monthlyTopUp = topUp
            )

            ResultScreen(
                initialAmount = amount,
                periodMonths = period,
                interestRate = rate,
                monthlyTopUp = topUp,
                onSave = {
                    viewModel.saveCalculation(
                        initialAmount = amount,
                        periodMonths = period,
                        interestRate = rate,
                        monthlyTopUp = topUp,
                        finalAmount = finalAmount,
                        interestEarned = interestEarned,
                        onSuccess = { savedId ->
                            android.util.Log.d("SaveCalculation", "Получен ID от ViewModel: $savedId")
                            Toast.makeText(navController.context, "Расчёт сохранён! ID: $savedId", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                onBackToMain = {
                    navController.popBackStack(Screen.Main.route, inclusive = false)
                }
            )
        }

        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = viewModel()
            HistoryScreen(
                onNavigateToDetail = { calculationId ->
                    navController.navigate(Screen.HistoryDetail.passId(calculationId))
                }
            )
        }

        composable(Screen.HistoryDetail.route) { backStackEntry ->
            val calculationIdStr = backStackEntry.arguments?.getString("calculationId") ?: "0"
            val calculationId = calculationIdStr.toLongOrNull() ?: 0

            Log.d("NavigationGraph", "Получен ID: $calculationId")

            HistoryDetailScreen(
                calculationId = calculationId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}