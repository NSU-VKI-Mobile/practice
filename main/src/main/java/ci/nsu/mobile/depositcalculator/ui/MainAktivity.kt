package ci.nsu.mobile.depositcalculator.ui

import android.os.Bundle
import androidx.navigation.NavType
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.depositcalculator.data.database.AppDatabase
import ci.nsu.mobile.depositcalculator.data.repository.DepositRepository
import ci.nsu.mobile.depositcalculator.ui.screens.*
import ci.nsu.mobile.depositcalculator.ui.theme.DepositCalculatorTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: DepositRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(context = this)
        repository = DepositRepository(depositDao = database.depositDao())

        setContent {
            DepositCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DepositCalculatorApp(repository = repository)
                }
            }
        }
    }
}

@Composable
fun DepositCalculatorApp(repository: DepositRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onCalculateClick = { navController.navigate("first_step") },
                onHistoryClick = { navController.navigate("history") },
                onExitClick = { /* Закрыть приложение или выйти */ }
            )
        }

        composable("first_step") {
            FirstStepScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSecondStep = { initialAmount: Double, periodMonths: Int ->  // Явно указываем типы
                    navController.navigate("second_step/$initialAmount/$periodMonths")
                }
            )
        }

        composable(
            route = "second_step/{initialAmount}/{periodMonths}",
            arguments = listOf(
                navArgument("initialAmount") { type = NavType.FloatType },
                navArgument("periodMonths") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val initialAmount = backStackEntry.arguments?.getFloat("initialAmount")?.toDouble() ?: 0.0
            val periodMonths = backStackEntry.arguments?.getInt("periodMonths") ?: 0

            SecondStepScreen(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { rate: Double, topUp: Double? ->  // Явно указываем типы
                    navController.navigate("result/$initialAmount/$periodMonths/$rate/$topUp")
                }
            )
        }

        composable(
            route = "result/{initialAmount}/{periodMonths}/{interestRate}/{monthlyTopUp}",
            arguments = listOf(
                navArgument("initialAmount") { type = NavType.FloatType },
                navArgument("periodMonths") { type = NavType.IntType },
                navArgument("interestRate") { type = NavType.FloatType },
                navArgument("monthlyTopUp") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val initialAmount = backStackEntry.arguments?.getFloat("initialAmount")?.toDouble() ?: 0.0
            val periodMonths = backStackEntry.arguments?.getInt("periodMonths") ?: 0
            val interestRate = backStackEntry.arguments?.getFloat("interestRate")?.toDouble() ?: 0.0
            val monthlyTopUp = backStackEntry.arguments?.getFloat("monthlyTopUp")?.toDouble() ?: 0.0

            ResultScreen(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                repository = repository,
                onSaveAndNavigateToMain = {
                    navController.popBackStack("main", inclusive = false)
                },
                onNavigateToMain = {
                    navController.popBackStack("main", inclusive = false)
                }
            )
        }
//sad1
        composable("history") {
            HistoryScreen(
                repository = repository,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}


