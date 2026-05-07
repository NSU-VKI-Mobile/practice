package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.MainViewModel
import ci.nsu.mobile.main.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        // Главный экран
        composable("main") {
            MainScreen(
                onCalculateClick = { navController.navigate("step1") },
                onHistoryClick = { navController.navigate("history") }
            )
        }

        // Шаг 1
        composable("step1") {
            Step1Screen(
                onBackClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onNextClick = { startAmount, months ->
                    navController.navigate("step2/$startAmount/$months")
                }
            )
        }

        // Шаг 2
        composable(
            route = "step2/{startAmount}/{months}",
            arguments = listOf(
                navArgument("startAmount") { type = NavType.FloatType },
                navArgument("months") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val startAmount =
                backStackEntry.arguments?.getFloat("startAmount")?.toDouble() ?: 0.0
            val months = backStackEntry.arguments?.getInt("months") ?: 0
            Step2Screen(
                startAmount = startAmount,
                months = months,
                onBackClick = { navController.popBackStack() },
                onCalculateClick = { rate, monthlyAddition ->
                    navController.navigate(
                        "result/$startAmount/$months/$rate/$monthlyAddition"
                    )
                }
            )
        }

        // Результат
        composable(
            route = "result/{startAmount}/{months}/{rate}/{monthlyAddition}",
            arguments = listOf(
                navArgument("startAmount") { type = NavType.FloatType },
                navArgument("months") { type = NavType.IntType },
                navArgument("rate") { type = NavType.FloatType },
                navArgument("monthlyAddition") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val startAmount = args.getFloat("startAmount").toDouble()
            val months = args.getInt("months")
            val rate = args.getFloat("rate").toDouble()
            val monthlyAddition = args.getFloat("monthlyAddition").toDouble()

            ResultScreen(
                startAmount = startAmount,
                months = months,
                rate = rate,
                monthlyAddition = monthlyAddition,
                viewModel = viewModel,
                onHomeClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        // История
        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onItemClick = { deposit ->
                    navController.navigate("history_detail/${deposit.id}")
                }
            )
        }

        // Детали истории
        composable(
            route = "history_detail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            HistoryDetailScreen(
                id = id,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}