package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.screens.HomeScreen
import ci.nsu.mobile.main.ui.screens.Step1Screen
import ci.nsu.mobile.main.ui.screens.Step2Screen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.screens.DetailScreen

object Routes {
    const val HOME = "home"
    const val STEP1 = "step1"
    const val STEP2 = "step2"
    const val RESULT = "result"
    const val HISTORY = "history"
    const val DETAIL = "detail/{calculationId}"
    fun detailRoute(id: Long) = "detail/$id"
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onCalculate = { navController.navigate(Routes.STEP1) },
                onHistory = { navController.navigate(Routes.HISTORY) },
                onClose = { /* handled in HomeScreen */ }
            )
        }
        composable(Routes.STEP1) {
            Step1Screen(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Routes.STEP2) }
            )
        }
        composable(Routes.STEP2) {
            Step2Screen(
                onBack = { navController.popBackStack() },
                onCalculate = { navController.navigate(Routes.RESULT) }
            )
        }
        composable(Routes.RESULT) {
            ResultScreen(
                onBack = { navController.popBackStack() },
                onHome = { navController.popBackStack(Routes.HOME, inclusive = false) }
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                onItemClick = { id -> navController.navigate(Routes.detailRoute(id)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("calculationId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("calculationId") ?: return@composable
            DetailScreen(
                calculationId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}