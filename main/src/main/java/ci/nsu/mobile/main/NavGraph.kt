package ci.nsu.mobile.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.ui.screen.DetailsScreen
import ci.nsu.mobile.main.ui.screen.HistoryScreen
import ci.nsu.mobile.main.ui.screen.MainScreen
import ci.nsu.mobile.main.ui.screen.ResultScreen
import ci.nsu.mobile.main.ui.screen.StepOneScreen
import ci.nsu.mobile.main.ui.screen.StepTwoScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    var calculationResult by remember { mutableStateOf<DepositCalculation?>(null) }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onNavigateToInput = { navController.navigate("step_one") },
                onNavigateToHistory = { navController.navigate("history") }
            )
        }

        composable("step_one") {
            StepOneScreen(
                onNavigateToNext = { amount, months ->
                    navController.navigate("step_two/$amount/$months")
                },
                onNavigateToMain = { navController.popBackStack("main", false) }
            )
        }

        composable(
            "step_two/{initialAmount}/{periodMonths}",
            arguments = listOf(
                navArgument("initialAmount") { type = NavType.FloatType },
                navArgument("periodMonths") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getFloat("initialAmount")?.toDouble() ?: 0.0
            val months = backStackEntry.arguments?.getInt("periodMonths") ?: 0
            StepTwoScreen(
                initialAmount = amount,
                periodMonths = months,
                onNavigateToResult = { result ->
                    calculationResult = result
                    navController.navigate("result")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("result") {
            calculationResult?.let { result ->
                ResultScreen(
                    result = result,
                    onNavigateToMain = { navController.popBackStack("main", false) }
                )
            }
        }

        composable("history") {
            HistoryScreen(
                onNavigateToDetails = { id ->
                    navController.navigate("details/$id")
                },
                onNavigateToMain = { navController.popBackStack("main", false) }
            )
        }

        composable(
            "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            DetailsScreen(
                id = id,
                onNavigateToMain = { navController.popBackStack("main", false) }
            )
        }
    }
}