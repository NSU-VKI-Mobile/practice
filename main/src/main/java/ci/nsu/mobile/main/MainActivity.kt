package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.presentation.viewmodel.MainParamsViewModel
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PracticeTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {

        composable("main") {
            MainScreen(
                onCalculateClick = { navController.navigate("mainParams") },
                onHistoryClick = { navController.navigate("history") }
            )
        }

        composable("mainParams") {
            MainParamsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = { amount, months ->
                    navController.navigate("additionalParams/$amount/$months")
                }
            )
        }

        composable(
            route = "additionalParams/{amount}/{months}",
            arguments = listOf(
                navArgument("amount") { type = NavType.FloatType },
                navArgument("months") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0
            val months = backStackEntry.arguments?.getInt("months") ?: 0
            val mainParametersData = MainParamsViewModel.MainParamsResult.Success(amount, months)

            AdditionalParamsScreen(
                mainParamsData = mainParametersData,
                onNavigateBack = { navController.popBackStack() },
                onNavigateResult = { topUp, rate ->
                    navController.navigate("result/$amount/$months/$topUp/$rate")
                }
            )
        }

        composable(
            route = "result/{amount}/{months}/{topUp}/{rate}",
            arguments = listOf(
                navArgument("amount") { type = NavType.FloatType },
                navArgument("months") { type = NavType.IntType },
                navArgument("topUp") { type = NavType.FloatType },
                navArgument("rate") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0
            val months = backStackEntry.arguments?.getInt("months") ?: 0
            val topUp = backStackEntry.arguments?.getFloat("topUp")?.toDouble() ?: 0.0
            val rate = backStackEntry.arguments?.getFloat("rate")?.toDouble() ?: 0.0

            ResultScreen(
                amount = amount,
                months = months,
                topUp = topUp,
                rate = rate,
                onNavigateHome = {
                    navController.popBackStack("main", inclusive = false)
                }
            )
        }

        composable("history") {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}