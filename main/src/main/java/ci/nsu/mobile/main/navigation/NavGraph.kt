package ci.nsu.mobile.main.navigation

import ci.nsu.mobile.main.ui.Step1Screen
import ci.nsu.mobile.main.ui.Step2Screen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ci.nsu.mobile.main.ui.DetailsScreen
import ci.nsu.mobile.main.ui.MainScreen
import ci.nsu.mobile.main.ui.ResultScreen
import ci.nsu.mobile.main.ui.HistoryScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {

        composable("main") { MainScreen(navController) }

        composable("step1") {
            Step1Screen(navController)
        }

        composable("step2/{amount}/{months}") { backStack ->
            val amount = backStack.arguments?.getString("amount") ?: "0"
            val months = backStack.arguments?.getString("months") ?: "0"
            Step2Screen(navController, amount, months)
        }
        composable("history") {
            HistoryScreen(navController)
        }

        composable("result/{amount}/{months}/{rate}/{topUp}") { backStack ->
            ResultScreen(
                navController,
                backStack.arguments?.getString("amount") ?: "0",
                backStack.arguments?.getString("months") ?: "0",
                backStack.arguments?.getString("rate") ?: "0",
                backStack.arguments?.getString("topUp") ?: "0"
            )
        }
        composable("details/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            DetailsScreen(navController, id)
        }
    }
}