package ci.nsu.moble.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.moble.main.data.DepositRepository
import ci.nsu.moble.main.deposit.DepositViewModel
import ci.nsu.moble.main.deposit.ResultScreen
import ci.nsu.moble.main.deposit.StepOneScreen
import ci.nsu.moble.main.deposit.StepTwoScreen
import ci.nsu.moble.main.history.HistoryDetailScreen
import ci.nsu.moble.main.history.HistoryScreen
import ci.nsu.moble.main.history.HistoryViewModel

@Composable
fun AppNavGraph(
    repository: DepositRepository,
    onExitApp: () -> Unit
) {
    val navController = rememberNavController()

    val depositViewModel: DepositViewModel = viewModel(
        factory = remember { DepositViewModel.Factory(repository) }
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = remember { HistoryViewModel.Factory(repository) }
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onCalculateClick = {
                    depositViewModel.resetAll()
                    navController.navigate("step_one")
                },
                onHistoryClick = {
                    navController.navigate("history")
                },
                onExitClick = onExitApp
            )
        }

        composable("step_one") {
            StepOneScreen(
                viewModel = depositViewModel,
                onBackHome = {
                    depositViewModel.resetAll()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNext = {
                    navController.navigate("step_two")
                }
            )
        }

        composable("step_two") {
            StepTwoScreen(
                viewModel = depositViewModel,
                onBack = { navController.popBackStack() },
                onCalculate = { navController.navigate("result") }
            )
        }

        composable("result") {
            ResultScreen(
                viewModel = depositViewModel,
                onBackHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("history") {
            HistoryScreen(
                viewModel = historyViewModel,
                onOpenDetail = { id ->
                    navController.navigate("history_detail/$id")
                },
                onBackHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "history_detail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            HistoryDetailScreen(
                repository = repository,
                calculationId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}